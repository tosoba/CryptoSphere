import Shared
import SwiftUI

struct TokenFeedView: View {
    private let component: TokenFeedComponent

    @StateObject @KotlinStateFlow private var feedItems: [TokenItem]
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?

    @State private var scrolledItemId: Int32?
    private var navigationToken: TokenItem? { feedItems.first }
    private var currentPresentedToken: TokenItem? { feedItems.first(where: { $0.id == scrolledItemId }) }
    private var navigateToTokenFeedToolbarItemVisible: Bool {
        scrolledItemId != nil && navigationToken?.id != scrolledItemId
    }

    @State private var tokenTagsGridMeasuredHeight: CGFloat = 120

    init(_ component: TokenFeedComponent) {
        self.component = component

        _feedItems = .init(component.viewModel.tokensPagingState.itemsSnapshotList)
        _loadStates = .init(component.viewModel.tokensPagingState.loadStates)
    }

    var body: some View {
        ZStack {
            switch onEnum(of: loadStates?.refresh) {
            case .loading, .none:
                LargeCircularProgressView()
            case .notLoading:
                feedView
            case .error:
                EmptyView()
            }
        }
        .animation(.default, value: loadStates?.refresh)
    }

    @ViewBuilder
    private var feedView: some View {
        GeometryReader { geometry in
            ScrollView(.vertical) {
                LazyVStack(spacing: 0) {
                    ForEach(Array(feedItems.enumerated()), id: \.element.id) { index, item in
                        itemView(item, at: index, in: geometry)
                            .id(item.id)
                    }
                }
                .scrollTargetLayout()
            }
            .scrollPosition(id: $scrolledItemId, anchor: .top)
            .ignoresSafeArea(.container, edges: .all)
            .scrollIndicators(.hidden)
            .scrollTargetBehavior(.paging)
            .indeterminateLinearProgressViewOverlay(loadState: loadStates?.append)
        }
        .navigationTitle(navigationToken?.symbol ?? "")
        .toolbar {
            if navigateToTokenFeedToolbarItemVisible {
                ToolbarItem(placement: .topBarTrailing) {
                    Button(
                        action: {
                            if let item = currentPresentedToken {
                                component.navigateToTokenFeed(item)
                            }
                        }
                    ) {
                        if let item = currentPresentedToken {
                            HStack(spacing: 4) {
                                Text(item.symbol)
                                Image(systemName: "chevron.forward")
                            }
                        } else {
                            Image(systemName: "chevron.forward")
                        }
                    }
                    .transition(.opacity.combined(with: .scale))
                }
            }
        }
        .animation(.default, value: navigateToTokenFeedToolbarItemVisible)
    }

    @ViewBuilder
    private func itemView(_ item: TokenItem, at _: Int, in geometry: GeometryProxy) -> some View {
        TokenFeedPagerItem(
            token: item,
            mainTokenTagNames: Set(feedItems.first?.tagNames ?? []),
            safeAreaInsets: geometry.safeAreaInsets,
            tokenTagsGridMeasuredHeight: $tokenTagsGridMeasuredHeight
        )
        .containerRelativeFrame([.vertical, .horizontal])
    }
}

// MARK: - Token Feed Pager Item

struct TokenFeedPagerItem: View {
    let token: TokenItem
    let mainTokenTagNames: Set<String>
    let safeAreaInsets: EdgeInsets
    @Binding var tokenTagsGridMeasuredHeight: CGFloat

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass
    @Environment(\.verticalSizeClass) private var verticalSizeClass
    
    var body: some View {
        Group {
            if verticalSizeClass == .compact {
                compactLayout
            } else {
                regularLayout
            }
        }
        .padding(safeAreaInsets)
        .padding()
    }

    @ViewBuilder
    private var compactLayout: some View {
        HStack(spacing: 16) {
            VStack(alignment: .center, spacing: 16) {
                tokenLogo
                symbolWithRank
                
                if !token.tagNames.isEmpty {
                    TokenTagsGridViewController(
                        token: token,
                        mainTokenTagNames: mainTokenTagNames,
                        rowCount: 3,
                        measuredHeight: $tokenTagsGridMeasuredHeight
                    )
                    .frame(height: $tokenTagsGridMeasuredHeight.wrappedValue)
                }
            }
            .frame(maxWidth: .infinity)

            tokenFeedParameters
                .frame(maxWidth: .infinity)
        }
    }

    @ViewBuilder
    private var regularLayout: some View {
        VStack(spacing: 16) {
            tokenLogo
            symbolWithRank

            if !token.tagNames.isEmpty {
                TokenTagsGridViewController(
                    token: token,
                    mainTokenTagNames: mainTokenTagNames,
                    rowCount: Int32(
                        min(
                            token.tagNames.count,
                            verticalSizeClass == .regular && horizontalSizeClass == .regular ? 5 : 3
                        )
                    ),
                    measuredHeight: $tokenTagsGridMeasuredHeight
                )
                .frame(height: $tokenTagsGridMeasuredHeight.wrappedValue)
            }

            tokenFeedParameters
        }
    }

    @ViewBuilder
    private var tokenLogo: some View {
        AsyncImage(url: URL(string: token.logoUrl)) { phase in
            switch phase {
            case let .success(image):
                image
                    .resizable()
                    .aspectRatio(contentMode: .fit)
            case .failure, .empty:
                Color.gray.opacity(0.2)
            @unknown default:
                Color.gray.opacity(0.2)
            }
        }
        .frame(maxWidth: 120, maxHeight: 120)
        .aspectRatio(1, contentMode: .fit)
        .clipShape(RoundedRectangle(cornerRadius: 16))
    }

    @ViewBuilder
    private var symbolWithRank: some View {
        HStack(alignment: .firstTextBaseline, spacing: 4) {
            Text("#\(token.cmcRank)")
                .font(.subheadline)
                .fontWeight(.medium)
                .padding(.horizontal, 4)
                .background(
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color.secondary.opacity(0.2))
                )

            Text(token.symbol)
                .font(.largeTitle)
                .fontWeight(.medium)
        }
    }

    @ViewBuilder
    private var tokenFeedParameters: some View {
        GeometryReader { geometry in
            TokenFeedParameterCardsColumn(
                parameters: Array(
                    TokenFeedParameterKt.tokenFeedParameters(token: token)
                        .prefix(Int(geometry.size.height / calculateTokenParameterCardHeight()))
                )
            )
        }
    }
    
    private func calculateTokenParameterCardHeight() -> CGFloat {
        let verticalPadding: CGFloat = 8 * 2
        let vStackSpacing: CGFloat = 4
        
        let footnoteHeight = "A".size(
            withAttributes: [.font: UIFont.preferredFont(forTextStyle: .footnote)]
        ).height
        let title3Height = "A".size(
            withAttributes: [.font: UIFont.preferredFont(forTextStyle: .title3)]
        ).height
        
        let hStackHeight = max(title3Height, footnoteHeight)
        
        return footnoteHeight + vStackSpacing + hStackHeight + verticalPadding
    }
}

// MARK: - Token Parameter Cards

struct TokenFeedParameterCardsColumn: View {
    let parameters: [TokenFeedParameter]

    var body: some View {
        VStack(spacing: 2) {
            ForEach(Array(parameters.enumerated()), id: \.offset) { index, parameter in
                TokenFeedParameterCard(
                    parameter: parameter,
                    cornerRadius: cornerRadius(for: index)
                )
            }
        }
    }

    private func cornerRadius(for index: Int) -> (top: CGFloat, bottom: CGFloat) {
        if parameters.count == 1 {
            return (16, 16)
        } else if index == 0 {
            return (16, 0)
        } else if index == parameters.count - 1 {
            return (0, 16)
        } else {
            return (0, 0)
        }
    }
}

struct TokenFeedParameterCard: View {
    let parameter: TokenFeedParameter
    let cornerRadius: (top: CGFloat, bottom: CGFloat)

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(String(parameter.label))
                .font(.caption)
                .foregroundStyle(.secondary)

            HStack(spacing: 8) {
                Text(parameter.valueFormat(parameter.value))
                    .font(.title3)
                    .fontWeight(.medium)

                if let valueChange = parameter.valueChange as? Double,
                   let valueChangeFormat = parameter.valueChangeFormat
                {
                    let changeText = valueChangeFormat(valueChange)
                    if !changeText.isEmpty {
                        Text(changeText)
                            .font(.footnote)
                            .foregroundColor(valueChange >= 0.0 ? .black : .white)
                            .padding(.horizontal, 4)
                            .background(
                                RoundedRectangle(cornerRadius: 4)
                                    .fill(valueChange >= 0.0 ? .green : .red)
                            )
                    }
                }
            }
        }
        .lineLimit(1)
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal, 16)
        .padding(.vertical, 8)
        .background(
            UnevenRoundedRectangle(
                topLeadingRadius: cornerRadius.top,
                bottomLeadingRadius: cornerRadius.bottom,
                bottomTrailingRadius: cornerRadius.bottom,
                topTrailingRadius: cornerRadius.top
            )
            .fill(Color(uiColor: .secondarySystemBackground))
        )
    }
}
