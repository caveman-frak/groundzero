package uk.co.bluegecko.ui.geometry.javafx.path;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import uk.co.bluegecko.ui.geometry.javafx.common.CommonPackage;
import uk.co.bluegecko.ui.geometry.javafx.event.StageReadyEvent;

@Slf4j
@SpringBootApplication(scanBasePackageClasses = {PathDisplay.class, CommonPackage.class})
public class PathDisplay extends Application {

	private ConfigurableApplicationContext context;

	@Override
	public void init() {
		log.info("Initialising application");
		context = new SpringApplicationBuilder()
				.sources(PathDisplay.class)
				.web(WebApplicationType.NONE)
				.run(getParameters().getRaw().toArray(new String[0]));
	}

	@Override
	public void stop() {
		log.info("Stopping application");
		context.close();
		Platform.exit();
	}

	@Override
	public void start(Stage stage) {
		log.info("Starting application");
		context.publishEvent(new StageReadyEvent(stage, true));
	}

	public static void main(String[] args) {
		launch(PathDisplay.class, args);
	}


}