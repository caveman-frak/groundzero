package uk.co.bluegecko.utility.bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.common.model.invoice.Item;

public class ItemBeanTest {

	private static final Object[][] ARGS = new Object[][]{
			new Object[]{"identifier", new UUID(10, 1), UUID.class},
			new Object[]{"shortName", "Device", String.class},
			new Object[]{"description", "A device, it does things!", String.class},
			new Object[]{"customisations", Map.of(), Map.class},
			new Object[]{"price", Money.of(9.95, "GBP"), Money.class}
	};

	private Item item;

	@BeforeEach
	void setUp() {
		item = Item.builder()
				.identifier(new UUID(10, 1))
				.shortName("Device")
				.description("A device, it does things!")
				.price(Money.of(9.95, "GBP"))
				.build();
	}

	@Test
	void itemFieldsWithReflection() {
		Field[] fields = Item.class.getDeclaredFields();

		assertThat(Arrays.stream(fields).map(Field::getName).toArray())
				.hasSize(5)
				.containsExactly("identifier", "shortName", "description", "customisations", "price");
		assertThat(Arrays.stream(fields).map(Field::getType).toArray())
				.hasSize(5)
				.containsExactly(UUID.class, String.class, String.class, Map.class, Money.class);
	}

	@Test
	void itemMethodsWithReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method[] methods = Item.class.getDeclaredMethods();

		assertThat(Arrays.stream(methods).map(Method::getName).toArray())
				.hasSize(9)
				.contains(
						"equals", "toString", "hashCode", "builder",
						"identifier", "shortName", "description", "customisations", "price");
		assertThat(Arrays.stream(methods).map(Method::getReturnType).toArray())
				.hasSize(9)
				.contains(
						boolean.class, String.class, int.class, Item.ItemBuilder.class,
						String.class, String.class, UUID.class, Money.class, Map.class);
		for (Object[] arg : ARGS) {
			assertThat(Item.class.getDeclaredMethod((String) arg[0]).invoke(item)).isEqualTo(arg[1]);
		}
	}

	@Test
	void itemConstructorWithReflection() {
		Constructor<?>[] constructors = Item.class.getDeclaredConstructors();

		assertThat(constructors).hasSize(1);
		assertThat(constructors[0].accessFlags()).contains(AccessFlag.PUBLIC);
		assertThat(constructors[0].getParameterTypes())
				.hasSize(5)
				.containsExactly(UUID.class, String.class, String.class, Map.class, Money.class);
	}

	@Test
	void itemBuilderWithReflection() {
		Class<?>[] inner = Item.class.getClasses();
		assertThat(inner).hasSize(1)
				.extracting(Class::getSimpleName).contains("ItemBuilder");
		Class<?> builder = inner[0];
		assertThat(builder.getDeclaredMethods()).hasSize(9)
				.extracting(Method::getName).contains(
						"build", "toString", "identifier", "shortName", "description",
						"customisation", "customisations", "clearCustomisations", "price");
	}

	@Test
	void newItemFromAllArgCtor() throws InvocationTargetException, InstantiationException, IllegalAccessException {
		Constructor<?> allArgCtor = Item.class.getDeclaredConstructors()[0];
		Object obj = allArgCtor.newInstance(ARGS[0][1], ARGS[1][1], ARGS[2][1], ARGS[3][1], ARGS[4][1]);
		if (obj instanceof Item it) {
			assertThat(it).isEqualTo(item);
		} else {
			fail("Not instance of Item: %s", obj.getClass().getSimpleName());
		}
	}

	@Test
	void newItemFromBuilder()
			throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Method builderMethod = Item.class.getDeclaredMethod("builder");
		Object obj = builderMethod.invoke(null);
		if (obj instanceof Item.ItemBuilder builder) {
			for (Object[] arg : ARGS) {
				builder.getClass().getDeclaredMethod((String) arg[0], (Class<?>) arg[2]).invoke(builder, arg[1]);
			}
			Object o = builder.getClass().getDeclaredMethod("build").invoke(builder);
			if (o instanceof Item i) {
				assertThat(i).isEqualTo(item);
			} else {
				fail("Not instance of Item: %s", o.getClass().getSimpleName());
			}
		} else {
			fail("Not instance of ItemBuilder: %s", obj.getClass().getSimpleName());
		}
	}

	@Test
	void newItemFromBuilderUsingFields()
			throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Method builderMethod = Item.class.getDeclaredMethod("builder");
		Object obj = builderMethod.invoke(null);
		if (obj instanceof Item.ItemBuilder builder) {
			for (Field field : Item.class.getDeclaredFields()) {
				builder.getClass().getDeclaredMethod(field.getName(), field.getType())
						.invoke(builder, findValue(ARGS, field.getName()));
			}
			Object o = builder.getClass().getDeclaredMethod("build").invoke(builder);
			if (o instanceof Item i) {
				assertThat(i).isEqualTo(item);
			} else {
				fail("Not instance of Item: %s", o.getClass().getSimpleName());
			}
		} else {
			fail("Not instance of ItemBuilder: %s", obj.getClass().getSimpleName());
		}
	}

	private Object findValue(Object[][] args, String name) {
		return Arrays.stream(args).filter(a -> name.equals(a[0])).map(a -> a[1]).findFirst().orElse(null);
	}

}