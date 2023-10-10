import com.jayway.jsonpath.JsonPath;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

//        String tekst = leesInvoerbestand("src/main/resources/fileTest1.txt");
//        getLog().info("json:\n" + json);

        String json = leesInvoerbestand("src/main/resources/fileTest1.txt");
        String widgetTitle = JsonPath.read(json, "$.widget.window.title");
        List<String> locationNames = JsonPath.read(json, "$.widget.window.locations[*].name");
        String locationsNamesString = String.join("\n", locationNames);

        Path path = Paths.get("src/main/resources/fileTest3.txt");
//        byte[] strToBytes = (tekst + "\n" + tekst2 + "\n").getBytes();
        byte[] strToBytes = (locationsNamesString + "\n").getBytes();
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