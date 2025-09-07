import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class TestRunner {
    
    public static void main(String[] args) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(selectPackage("converter"))
            .build();

        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        
        System.out.println("=== Market Converter Test Suite ===");
        System.out.println("Running tests...\n");
        
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        
        System.out.println("\n=== Test Results Summary ===");
        System.out.printf("Tests found: %d%n", summary.getTestsFoundCount());
        System.out.printf("Tests successful: %d%n", summary.getTestsSucceededCount());
        System.out.printf("Tests failed: %d%n", summary.getTestsFailedCount());
        System.out.printf("Tests skipped: %d%n", summary.getTestsSkippedCount());
        
        if (summary.getTestsFailedCount() > 0) {
            System.out.println("\n=== Failures ===");
            summary.getFailures().forEach(failure -> {
                System.out.println("[FAIL] " + failure.getTestIdentifier().getDisplayName());
                System.out.println("   " + failure.getException().getMessage());
            });
        }
        
        if (summary.getTestsSucceededCount() == summary.getTestsFoundCount()) {
            System.out.println("\n[SUCCESS] All tests passed!");
        } else {
            System.out.println("\n[FAIL] Some tests failed.");
        }
        
        // Exit with error code if tests failed
        System.exit(summary.getTestsFailedCount() > 0 ? 1 : 0);
    }
}