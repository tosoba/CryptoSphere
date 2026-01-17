import Shared

extension Optional where Wrapped == CombinedLoadStates {
    func canLoadMoreItems() -> Bool {
        switch onEnum(of: self?.append) {
        case let .notLoading(state):
            return !state.endOfPaginationReached
        default:
            return false
        }
    }
}
