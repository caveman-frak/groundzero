package uk.co.bluegecko.utility.bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.beans.ConstructorProperties;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.FluentPropertyBeanIntrospector;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.common.model.invoice.Address;

public class AddressBeanTest {

	private final String[][] ARGS = new String[][]{
			new String[]{"name", "The Prime Minister"},
			new String[]{"company", "Her Majesties Government"},
			new String[]{"building", "10"},
			new String[]{"street", "Downing Street"},
			new String[]{"locality", null},
			new String[]{"town", "London"},
			new String[]{"state", null},
			new String[]{"postcode", "SW1A 2AA"},
			new String[]{"country", "United Kingdom"}};

	private Address address;

	@BeforeEach
	void setUp() {
		address = Address.builder()
				.name("The Prime Minister")
				.company("Her Majesties Government")
				.building("10")
				.street("Downing Street")
				.town("London")
				.postcode("SW1A 2AA")
				.country("United Kingdom")
				.build();

		// need this to handle chained setters
		PropertyUtils.addBeanIntrospector(new FluentPropertyBeanIntrospector());
	}

	@Test
	void addressPropertiesWithBeanMap() {
		BeanMap map = new BeanMap(address);

		assertThat(map.keySet()).contains(
				"name", "company", "building", "street", "locality", "town", "state", "postcode", "country");
		assertThat(map.keySet()).containsExactly(
				"country", "town", "street", "locality", "name", "postcode", "company", "state", "class", "building");
		assertThat(map.values()).contains(
				"The Prime Minister", "Her Majesties Government", "10", "Downing Street",
				"London", "SW1A 2AA", "United Kingdom");
		assertThat(map.entrySet()).contains(
				Map.entry("name", "The Prime Minister"),
				Map.entry("company", "Her Majesties Government"),
				Map.entry("building", "10"),
				Map.entry("street", "Downing Street"),
				Map.entry("town", "London"),
				Map.entry("postcode", "SW1A 2AA"),
				Map.entry("country", "United Kingdom"));
	}

	@Test
	void addressFieldsWithReflection() {
		Field[] fields = Address.class.getDeclaredFields();

		assertThat(Arrays.stream(fields).map(Field::getName).toArray())
				.hasSize(9)
				.containsExactly(
						"name", "company", "building", "street", "locality", "town", "state", "postcode", "country");
	}

	@Test
	void addressMethodsWithReflection() {
		Method[] methods = Address.class.getDeclaredMethods();

		assertThat(Arrays.stream(methods).map(Method::getName).filter(s -> s.startsWith("get")).toArray())
				.hasSize(9)
				.contains("getName", "getCompany", "getBuilding", "getStreet", "getLocality", "getTown", "getState",
						"getPostcode", "getCountry");

		assertThat(Arrays.stream(methods).map(Method::getName).filter(s -> s.startsWith("set")).toArray())
				.hasSize(9)
				.contains("setName", "setCompany", "setBuilding", "setStreet", "setLocality", "setTown", "setState",
						"setPostcode", "setCountry");
	}

	@Test
	void addressConstructorWithReflection() {
		Constructor<?>[] constructors = Address.class.getDeclaredConstructors();

		assertThat(constructors).hasSize(2);
		Constructor<?> allArgCtor = constructors[0];
		assertThat(allArgCtor.accessFlags()).containsExactly(AccessFlag.PUBLIC);
		assertThat(allArgCtor.getParameterCount()).isEqualTo(9);
		assertThat(allArgCtor.getParameterTypes()).hasSize(9)
				.containsExactly(String.class, String.class, String.class, String.class, String.class, String.class,
						String.class, String.class, String.class);
		ConstructorProperties[] constructorProperties = allArgCtor.getDeclaredAnnotationsByType(
				ConstructorProperties.class);
		assertThat(constructorProperties).hasSize(1);
		assertThat(constructorProperties[0].value()).hasSize(9)
				.containsExactly(
						"name", "company", "building", "street", "locality", "town", "state", "postcode", "country");
		Constructor<?> noArgCtor = constructors[1];
		assertThat(noArgCtor.accessFlags()).containsExactly(AccessFlag.PUBLIC);
		assertThat(noArgCtor.getParameterCount()).isEqualTo(0);
	}

	@Test
	void addressFieldConstants() {
		Class<?>[] inner = Address.class.getClasses();
		assertThat(inner).hasSize(2)
				.extracting(Class::getSimpleName).contains("AddressBuilder", "Fields");
		Class<?> fields = inner[1];
		assertThat(fields.isEnum()).isTrue();
		assertThat(fields.getEnumConstants()).hasSize(9)
				.extracting(Object::toString).containsExactly(
						"name", "company", "building", "street", "locality", "town", "state", "postcode", "country");

		assertThat(Address.Fields.values()).hasSize(9)
				.extracting(Enum::name).containsExactly(
						"name", "company", "building", "street", "locality", "town", "state", "postcode", "country");
	}

	@Test
	void addressBuilderWithReflection() {
		Class<?>[] inner = Address.class.getClasses();
		assertThat(inner).hasSize(2)
				.extracting(Class::getSimpleName).contains("AddressBuilder", "Fields");
		Class<?> builder = inner[0];
		assertThat(builder.getDeclaredMethods()).hasSize(11)
				.extracting(Method::getName).contains(
						"name", "company", "building", "street", "locality", "town", "state", "postcode", "country",
						"build", "toString");
	}

	@Test
	void newAddressFromAllArgCtor() throws InvocationTargetException, InstantiationException, IllegalAccessException {
		Constructor<?> allArgCtor = Address.class.getDeclaredConstructors()[0];
		Object obj = allArgCtor.newInstance(
				ARGS[0][1], ARGS[1][1], ARGS[2][1], ARGS[3][1], ARGS[4][1], ARGS[5][1], ARGS[6][1], ARGS[7][1],
				ARGS[8][1]);
		if (obj instanceof Address add) {
			assertThat(add).isEqualTo(address);
		} else {
			fail("Not instance of Address: %s", obj.getClass().getSimpleName());
		}
	}

	@Test
	void newAddressFromNoArgCtor()
			throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
		Constructor<?> noArgCtor = Address.class.getDeclaredConstructors()[1];
		Object obj = noArgCtor.newInstance();
		if (obj instanceof Address add) {
			// set each field in turn
			for (String[] arg : ARGS) {
				PropertyUtils.getPropertyDescriptor(add, arg[0]).getWriteMethod().invoke(add, arg[1]);
			}
			assertThat(add).isEqualTo(address);
		} else {
			fail("Not instance of Address: %s", obj.getClass().getSimpleName());
		}
	}

	@Test
	void newAddressFromBuilder()
			throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Method builderMethod = Address.class.getDeclaredMethod("builder");
		Object obj = builderMethod.invoke(null);
		if (obj instanceof Address.AddressBuilder builder) {
			// set each field in turn
			for (String[] arg : ARGS) {
				builder.getClass().getDeclaredMethod(arg[0], String.class).invoke(builder, arg[1]);
			}
			Object o = builder.getClass().getDeclaredMethod("build").invoke(builder);
			if (o instanceof Address add) {
				assertThat(add).isEqualTo(address);
			} else {
				fail("Not instance of Address: %s", obj.getClass().getSimpleName());
			}
		} else {
			fail("Not instance of AddressBuilder: %s", obj.getClass().getSimpleName());
		}
	}

}