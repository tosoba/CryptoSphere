import BackgroundTasks
import Shared
import UIKit

class TokensSyncWorker {
    private static let TAG = "TokensSyncWorker"

    private var dependencyContainer: DependencyContainer {
        (UIApplication.shared.delegate as! AppDelegate).dependencyContainer
    }

    func performTokensSync(task: BGProcessingTask) {
        Task(priority: .background) {
            do {
                try await performFullTokensSyncUseCase(container: dependencyContainer)
                task.setTaskCompleted(success: true)
            } catch {
                LoggerKt.kermit.e(messageString: error.localizedDescription, throwable: nil, tag: TokensSyncWorker.TAG)
                task.setTaskCompleted(success: false)
            }
        }
    }
}
