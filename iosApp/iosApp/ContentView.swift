import SwiftUI
import shared // Import your shared KMM module

struct ContentView: View {
    @StateObject private var viewModel = ExerciseSearchViewModel()

    var body: some View {
        VStack {
            TextField("Search exercises", text: $viewModel.searchText)
                .padding()
                .textFieldStyle(RoundedBorderTextFieldStyle())

            List(viewModel.searchResults, id: \.id) { exercise in
                VStack(alignment: .leading) {
                    Text(exercise.name)
                        .font(.headline)
                    if let primaryMuscles = exercise.primaryMuscles {
                        Text(primaryMuscles.joined(separator: ", "))
                            .font(.subheadline)
                    }
                }
            }
        }
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
