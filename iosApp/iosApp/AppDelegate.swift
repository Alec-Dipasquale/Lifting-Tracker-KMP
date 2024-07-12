import UIKit
import shared // Import the shared module

class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Initialize and populate the database
        initializeDatabase()
        return true
    }

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
            let sceneConfig: UISceneConfiguration = UISceneConfiguration(name: nil, sessionRole: connectingSceneSession.role)
            sceneConfig.delegateClass = SceneDelegate.self
            return sceneConfig
        }

    private func initializeDatabase() {
        let dbFactory = DBFactory()
        let db = dbFactory.createDatabase()
        Task {
            do {
                NSLog("Populating database") // Log statement
                try await dbFactory.populateExerciseDetailsDatabase()
                let exercises = try await db.exerciseDao().getAllExercises()
                exercises.forEach { exercise in
                    NSLog("[Database123] Exercise: \(exercise)")
                }
                NSLog("[Database123] Database populated")
            } catch {
                NSLog("[Database123] Error populating database: \(error.localizedDescription)")
            }
        }
    }
}

