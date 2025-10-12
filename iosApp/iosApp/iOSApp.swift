import Shared
import SwiftUI

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate

    var body: some Scene {
        WindowGroup {
            RootView(appDelegate.root)
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    private static let TAG = "AppDelegate"

    private var stateKeeper = StateKeeperDispatcherKt.StateKeeperDispatcher(savedState: nil)
    private lazy var backgroundJobsManager: IosBackgroundJobsManager = IosBackgroundJobsManager()

    lazy var dependencyContainer = DependencyContainerBuilderKt.buildDependencyContainer(
        backgroundJobsManager: backgroundJobsManager
    )

    fileprivate lazy var root: RootComponent = dependencyContainer.createRootComponent(
        DefaultComponentContext(
            lifecycle: ApplicationLifecycle(),
            stateKeeper: stateKeeper,
            instanceKeeper: nil,
            backHandler: nil
        )
    )

    func application(_: UIApplication, didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        backgroundJobsManager.registerPeriodicTokensSync()
        enqueuePeriodicTokensSync()
        return true
    }

    func application(_: UIApplication, shouldSaveSecureApplicationState coder: NSCoder) -> Bool {
        StateKeeperUtilsKt.save(coder: coder, state: stateKeeper.save())
        return true
    }

    func application(_: UIApplication, shouldRestoreSecureApplicationState coder: NSCoder) -> Bool {
        stateKeeper = StateKeeperDispatcherKt.StateKeeperDispatcher(savedState: StateKeeperUtilsKt.restore(coder: coder))
        return true
    }

    private func enqueuePeriodicTokensSync() {
        Task(priority: .background) {
            do {
                try await enqueuePeriodicTokensSyncUseCase(container: dependencyContainer)
            } catch {
                LoggerKt.kermit.e(messageString: error.localizedDescription, throwable: nil, tag: AppDelegate.TAG)
            }
        }
    }
}
