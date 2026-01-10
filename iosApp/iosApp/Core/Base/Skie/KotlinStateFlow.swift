import Shared
import SwiftUI

@propertyWrapper
final class KotlinStateFlow<T>: ObservableObject {
    @Published var wrappedValue: T
    private var task: Task<Void, Never>?

    init(_ stateFlow: SkieSwiftStateFlow<T>) {
        wrappedValue = stateFlow.value
        task = Task { @MainActor [weak self] in
            for await item in stateFlow {
                self?.wrappedValue = item
            }
        }
    }

    deinit {
        task?.cancel()
    }
}

extension ObservedObject {
    init<F>(_ stateFlow: SkieSwiftStateFlow<F>) where ObjectType == KotlinStateFlow<F> {
        self.init(wrappedValue: KotlinStateFlow(stateFlow))
    }
}

extension StateObject {
    init<F>(_ stateFlow: SkieSwiftStateFlow<F>) where ObjectType == KotlinStateFlow<F> {
        self.init(wrappedValue: KotlinStateFlow(stateFlow))
    }
}
