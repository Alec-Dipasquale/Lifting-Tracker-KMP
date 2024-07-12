import Foundation
import Combine
import shared // Import your shared KMM module

@MainActor
class ExerciseSearchViewModel: ObservableObject {
    @Published var searchText: String = ""
    @Published var searchResults: [ExerciseDetails] = []

    private var db: AppDatabase
    private var cancellables = Set<AnyCancellable>()

    init() {
        let dbFactory = DBFactory().createDatabase()
        self.db = dbFactory

        // Observe searchText changes and perform search
        $searchText
            .debounce(for: .milliseconds(300), scheduler: RunLoop.main)
            .removeDuplicates()
            .sink { [weak self] searchTerm in
                guard let self = self else { return }
                Task {
                    await self.searchDatabase(with: searchTerm)
                }
            }
            .store(in: &cancellables)
    }

    private func searchDatabase(with term: String) async {
        guard !term.isEmpty else {
            self.searchResults = []
            return
        }

        do {
            let results = try await searchExercises(term: term)
            self.searchResults = results
        } catch {
            print("Error searching exercises: \(error)")
        }
    }

    private func searchExercises(term: String) async throws -> [ExerciseDetails] {
        return try await KotlinUtils.runBlocking {
            return try await self.db.exerciseDao().searchExercises(search: term)
        }
    }
}



public class KotlinUtils {
    public static func runBlocking<T>(_ block: @escaping () async throws -> T) async throws -> T {
        return try await withCheckedThrowingContinuation { continuation in
            Task {
                do {
                    let result = try await block()
                    continuation.resume(returning: result)
                } catch {
                    continuation.resume(throwing: error)
                }
            }
        }
    }
}
