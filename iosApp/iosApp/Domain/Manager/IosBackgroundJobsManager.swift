import BackgroundTasks
import Foundation
import Shared
import UIKit

class IosBackgroundJobsManager: BackgroundJobsManager {
    private static let TAG = "IosBackgroundJobsManager"
    private static let TOKENS_SYNC_IDENTIFIER = "tokens_sync"

    func registerPeriodicTokensSync() {
        BGTaskScheduler.shared.register(
            forTaskWithIdentifier: IosBackgroundJobsManager.TOKENS_SYNC_IDENTIFIER,
            using: nil
        ) { task in
            TokensSyncWorker().performTokensSync(task: task as! BGProcessingTask)
        }
    }

    func enqueuePeriodicTokensSync() {
        let request = BGProcessingTaskRequest(identifier: IosBackgroundJobsManager.TOKENS_SYNC_IDENTIFIER)
        request.earliestBeginDate = Date(timeIntervalSinceNow: 60 * 60 * 24)
        do {
            try BGTaskScheduler.shared.submit(request)
        } catch {
            LoggerKt.kermit.e(messageString: error.localizedDescription, throwable: nil, tag: IosBackgroundJobsManager.TAG)
        }
    }
}
