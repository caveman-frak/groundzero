package uk.co.bluegecko.ui.geometry.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import uk.co.bluegecko.ui.geometry.calc.GeometryCalculator;
import uk.co.bluegecko.ui.geometry.javafx.event.StageReadyEvent;

@Slf4j
@SpringBootApplication
public class GeometryDisplay extends Application {

	private ConfigurableApplicationContext context;

	@Override
	public void init() {
		context = new SpringApplicationBuilder()
				.web(WebApplicationType.NONE)
				.sources(GeometryDisplay.class, GeometryCalculator.class)
				.run(getParameters().getRaw().toArray(new String[0]));
		log.info("Initialising application");
	}

	@Override
	public void stop() {
		log.info("Stopping application");
		context.close();
		Platform.exit();
	}

	@Override
	public void start(Stage stage) {
		context.publishEvent(new StageReadyEvent(stage, true));
		log.info("Starting application");
	}

	public static void main(String[] args) {
		launch(GeometryDisplay.class, args);
	}

}