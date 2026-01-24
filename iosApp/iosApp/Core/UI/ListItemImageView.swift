import SwiftUI

struct ListItemImageView: View {
    let imageUrl: String?

    var body: some View {
        AsyncImage(
            url: URL(string: imageUrl ?? ""),
            transaction: Transaction(animation: .default)
        ) { phase in
            switch phase {
            case .empty:
                ProgressView()
            case let .success(image):
                image
                    .resizable()
                    .scaledToFill()
            default:
                Image(systemName: "bitcoinsign")
                    .resizable()
                    .scaledToFit()
            }
        }
        .frame(width: 48, height: 48)
        .clipShape(RoundedRectangle(cornerRadius: 8))
    }
}
