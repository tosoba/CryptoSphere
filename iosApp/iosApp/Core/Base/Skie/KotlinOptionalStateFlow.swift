import Shared
import SwiftUI

@propertyWrapper
final class KotlinOptionalStateFlow<T>: ObservableObject {
    @Published var wrappedValue: T?
    private var task: Task<Void, Never>?

    init(_ stateFlow: SkieSwiftOptionalStateFlow<T>) {
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
    init<F>(_ stateFlow: SkieSwiftOptionalStateFlow<F>) where ObjectType == KotlinOptionalStateFlow<F> {
        self.init(wrappedValue: KotlinOptionalStateFlow(stateFlow))
    }
}

extension StateObject {
    init<F>(_ stateFlow: SkieSwiftOptionalStateFlow<F>) where ObjectType == KotlinOptionalStateFlow<F> {
        self.init(wrappedValue: KotlinOptionalStateFlow(stateFlow))
    }
}
