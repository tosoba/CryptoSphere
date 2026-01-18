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
            .overlay(alignment: .bottom) {
                let progressVisible = if case .loading = onEnum(of: loadStates?.append) { true } else { false }
                IndeterminateLinearProgressView(isVisible: progressVisible)
            }
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
        let mainTokenTagNames = Set(feedItems.first?.tagNames ?? [])
        let isCompactHeight = geometry.size.height < 600

        TokenFeedPagerItem(
            token: item,
            mainTokenTagNames: mainTokenTagNames,
            isCompactHeight: isCompactHeight,
            availableHeight: geometry.size.height,
            safeAreaInsets: geometry.safeAreaInsets
        )
        .containerRelativeFrame([.vertical, .horizontal])
    }
}

// MARK: - Token Feed Pager Item

struct TokenFeedPagerItem: View {
    let token: TokenItem
    let mainTokenTagNames: Set<String>
    let isCompactHeight: Bool
    let availableHeight: CGFloat
    let safeAreaInsets: EdgeInsets

    var body: some View {
        Group {
            if isCompactHeight {
                compactLayout
            } else {
                regularLayout
            }
        }
        .padding(.horizontal)
        .padding(.top, max(safeAreaInsets.top, 16))
        .padding(.bottom, max(safeAreaInsets.bottom, 16))
    }

    @ViewBuilder
    private var compactLayout: some View {
        HStack(spacing: 32) {
            VStack(alignment: .center, spacing: 8) {
                tokenLogo
                symbolWithRank
                if !token.tagNames.isEmpty {
                    tagGrid(rowCount: 2)
                }
                Spacer()
            }
            .frame(maxWidth: .infinity)

            VStack {
                Spacer()
                tokenParameters
                Spacer()
            }
            .frame(maxWidth: .infinity)
        }
    }

    @ViewBuilder
    private var regularLayout: some View {
        VStack(alignment: .center, spacing: 8) {
            tokenLogo
            symbolWithRank

            if !token.tagNames.isEmpty {
                let rowCount = min(token.tagNames.count, availableHeight > 900 ? 5 : 3)
                tagGrid(rowCount: rowCount)
                    .padding(.top, 8)
            }

            tokenParameters
                .padding(.top, 16)

            Spacer()
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
        .frame(maxWidth: 96, maxHeight: 96)
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
    private func tagGrid(rowCount: Int) -> some View {
        ScrollView(.horizontal, showsIndicators: false) {
            LazyHGrid(
                rows: Array(repeating: GridItem(.fixed(32), spacing: 4), count: rowCount),
                alignment: .top,
                spacing: 4
            ) {
                ForEach(token.tagNames, id: \.self) { tagName in
                    TagChip(
                        name: tagName,
                        isSelected: mainTokenTagNames.contains(tagName)
                    )
                }
            }
        }
        .frame(maxWidth: .infinity)
        .frame(height: CGFloat(rowCount * 32 + (rowCount - 1) * 4))
    }

    @ViewBuilder
    private var tokenParameters: some View {
        let parameters = buildParameters()
        let maxCards = Int(availableHeight / 70) // Approximate card height
        let displayedParameters = Array(parameters.prefix(maxCards))

        TokenParameterCardsColumn(parameters: displayedParameters)
    }

    private func buildParameters() -> [TokenParameter] {
        let valueChangeFormat: (Double?) -> String = { value in
            guard let value = value else { return "" }
            let formatted = String(format: " %.2f%% ", value)
            return value >= 0 ? "+\(formatted)" : formatted
        }

        var params: [TokenParameter] = [
            TokenParameter(
                label: "Price",
                value: token.quote.price,
                valueFormat: { "$\(formatNumber($0))" },
                valueChange: token.quote.percentChange24h,
                valueChangeFormat: valueChangeFormat
            ),
            TokenParameter(
                label: "Volume 24h",
                value: token.quote.volume24h,
                valueChange: token.quote.volumeChange24h,
                valueChangeFormat: valueChangeFormat
            ),
            TokenParameter(
                label: "Market Cap",
                value: token.quote.marketCap
            ),
            TokenParameter(
                label: "Circulating Supply",
                value: token.circulatingSupply
            ),
            TokenParameter(
                label: "Total Supply",
                value: token.totalSupply
            ),
        ]

        if let maxSupply = token.maxSupply {
            params.append(
                TokenParameter(
                    label: "Max Supply",
                    value: Double(truncating: maxSupply)
                )
            )
        }

        return params
    }

    private func formatNumber(_ value: Double) -> String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 2
        return formatter.string(from: NSNumber(value: value)) ?? "\(value)"
    }
}

// MARK: - Tag Chip

struct TagChip: View {
    let name: String
    let isSelected: Bool

    var body: some View {
        Text(name)
            .font(.caption)
            .lineLimit(1)
            .padding(.horizontal, 12)
            .padding(.vertical, 6)
            .frame(height: 32)
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(isSelected ? Color.accentColor.opacity(0.2) : Color.secondary.opacity(0.1))
            )
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(isSelected ? Color.accentColor : Color.clear, lineWidth: 1)
            )
    }
}

// MARK: - Token Parameter Cards

struct TokenParameterCardsColumn: View {
    let parameters: [TokenParameter]

    var body: some View {
        VStack(spacing: 2) {
            ForEach(Array(parameters.enumerated()), id: \.offset) { index, parameter in
                TokenParameterCard(
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

struct TokenParameterCard: View {
    let parameter: TokenParameter
    let cornerRadius: (top: CGFloat, bottom: CGFloat)

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(parameter.label)
                .font(.caption)
                .foregroundStyle(.secondary)
                .lineLimit(1)

            HStack(alignment: .firstTextBaseline, spacing: 4) {
                Text(parameter.valueFormat(parameter.value))
                    .font(.title3)
                    .fontWeight(.medium)
                    .lineLimit(1)

                if let valueChange = parameter.valueChange,
                   let valueChangeFormat = parameter.valueChangeFormat
                {
                    let changeText = valueChangeFormat(valueChange)
                    if !changeText.isEmpty {
                        Text(changeText)
                            .font(.caption)
                            .fontWeight(valueChange >= 0 ? .regular : .medium)
                            .foregroundColor(valueChange >= 0 ? .black : .white)
                            .padding(.horizontal, 4)
                            .background(
                                RoundedRectangle(cornerRadius: 4)
                                    .fill(valueChange >= 0 ? Color.green : Color.red)
                            )
                    }
                }
            }
        }
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

// MARK: - Token Parameter Model

struct TokenParameter {
    let label: String
    let value: Double
    let valueFormat: (Double) -> String
    let valueChange: Double?
    let valueChangeFormat: ((Double?) -> String)?

    init(
        label: String,
        value: Double,
        valueFormat: @escaping (Double) -> String = { value in
            let formatter = NumberFormatter()
            formatter.numberStyle = .decimal
            formatter.maximumFractionDigits = 0
            let formatted = formatter.string(from: NSNumber(value: value)) ?? "\(Int(value))"
            return "$\(formatted)"
        },
        valueChange: Double? = nil,
        valueChangeFormat: ((Double?) -> String)? = nil
    ) {
        self.label = label
        self.value = value
        self.valueFormat = valueFormat
        self.valueChange = valueChange
        self.valueChangeFormat = valueChangeFormat
    }
}
