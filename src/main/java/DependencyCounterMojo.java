import com.jayway.jsonpath.JsonPath;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Mojo(name = "dependency-counter", defaultPhase = LifecyclePhase.COMPILE)
public class DependencyCounterMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "scope")
    String scope;

    private void displayDependencies() {
        List<Dependency> dependencies = project.getDependencies();
        long numDependencies = dependencies.stream()
                .filter(d -> (scope == null || scope.isEmpty()) || scope.equals(d.getScope()))
                .count();
        getLog().info("Number of dependencies: " + numDependencies);
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        displayDependencies();

        String jsonHeaderString = leesInvoerbestand("src/main/resources/header.json");
        List<String> locationNames = JsonPath.read(jsonHeaderString, "$.widget.window.locations[*].name");
        String locationNamesHeaderString = String.join("\n", locationNames);

        String jsonRequestString = leesInvoerbestand("src/main/resources/wazoRequest.json");
        List<String> locationNamesRequest = JsonPath.read(jsonRequestString, "$.widget.window.locations[*].name");
        String locationNamesRequestString = String.join("\n", locationNamesRequest);

        Path path = Paths.get("src/main/resources/properties.json");
        byte[] strToBytes = (locationNamesHeaderString + "\n" + locationNamesRequestString + "\n").getBytes();
        try {
            Files.write(path, strToBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String leesInvoerbestand(String bestandsNaam) {
        Path path = Paths.get(bestandsNaam);
        String read;
        try {
            read = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException("Fout bij het lezen van bestandsNaam");
        }
        return read;
    }
}