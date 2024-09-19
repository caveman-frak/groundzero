//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.beanio.internal.config.annotation;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import org.beanio.BeanIOConfigurationException;
import org.beanio.annotation.Field;
import org.beanio.annotation.Fields;
import org.beanio.annotation.Group;
import org.beanio.annotation.Record;
import org.beanio.annotation.Segment;
import org.beanio.internal.config.ComponentConfig;
import org.beanio.internal.config.FieldConfig;
import org.beanio.internal.config.GroupConfig;
import org.beanio.internal.config.PropertyConfig;
import org.beanio.internal.config.RecordConfig;
import org.beanio.internal.config.SegmentConfig;
import org.beanio.internal.util.TypeUtil;

public class AnnotationParser {

	private static final Comparator<ComponentConfig> ORDINAL_COMPARATOR = new Comparator<>() {
		@Override
		public int compare(ComponentConfig c1, ComponentConfig c2) {
			Integer o1 = c1.getOrdinal();
			Integer o2 = c2.getOrdinal();
			if (o1 == null) {
				return o2 == null ? 0 : 1;
			} else {
				return o2 == null ? -1 : o1.compareTo(o2);
			}
		}
	};

	public AnnotationParser() {
	}

	public static GroupConfig createGroupConfig(ClassLoader classLoader, String type) {
		Class<?> clazz = TypeUtil.toBeanType(classLoader, type);
		return clazz == null ? null : createGroupConfig(clazz);
	}

	public static GroupConfig createGroupConfig(Class<?> clazz) {
		Group group = clazz.getAnnotation(Group.class);
		if (group == null) {
			return null;
		} else {
			String name = toValue(group.name());
			if (name == null) {
				name = Introspector.decapitalize(clazz.getSimpleName());
			}

			TypeInfo info = new TypeInfo();
			info.name = name;
			info.type = clazz;
			return createGroup(info, group);
		}
	}

	public static RecordConfig createRecordConfig(ClassLoader classLoader, String type) {
		Class<?> clazz = TypeUtil.toBeanType(classLoader, type);
		return clazz == null ? null : createRecordConfig(clazz);
	}

	public static RecordConfig createRecordConfig(Class<?> clazz) {
		Record record = clazz.getAnnotation(Record.class);
		if (record == null) {
			return null;
		} else {
			String name = toValue(record.name());
			if (name == null) {
				name = Introspector.decapitalize(clazz.getSimpleName());
			}

			TypeInfo info = new TypeInfo();
			info.name = name;
			info.type = clazz;
			return createRecord(info, record);
		}
	}

	private static void addAllChildren(ComponentConfig config, Class<?> clazz) {
		Class<?> superclazz = clazz.getSuperclass();
		if (superclazz != null && superclazz != Object.class) {
			addAllChildren(config, superclazz);
		}

		Class[] var3 = clazz.getInterfaces();
		int var4 = var3.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			Class<?> intf = var3[var5];
			addAllChildren(config, intf);
		}

		addChildren(config, clazz);
	}

	private static void handleConstructor(ComponentConfig config, Class<?> clazz) {
		try {
			Constructor[] var14 = clazz.getDeclaredConstructors();
			int var3 = var14.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				Constructor<?> constructor = var14[var4];
				Class<?>[] parameters = constructor.getParameterTypes();
				Type[] parameterTypes = constructor.getGenericParameterTypes();
				Annotation[][] annotations = constructor.getParameterAnnotations();

				for (int i = 0; i < annotations.length; ++i) {
					Field fa = null;

					for (int j = 0; j < annotations[i].length; ++j) {
						if (annotations[i][j].annotationType() == Field.class) {
							fa = (Field) annotations[i][j];
							break;
						}
					}

					if (fa != null) {
						TypeInfo info = new TypeInfo();
						info.carg = i + 1;
						info.name = toValue(fa.name());
						info.type = parameters[i];
						info.genericType = parameterTypes[i];
						config.add(createField(info, fa));
					}
				}
			}

		} catch (IllegalArgumentException var12) {
			IllegalArgumentException ex = var12;
			throw new BeanIOConfigurationException(
					"Invalid @Field annotation on a constructor parameter in class '" + clazz.getName() + "': "
							+ ex.getMessage(), ex);
		}
	}

	private static void addChildren(ComponentConfig config, Class<?> parent) {
		if (config.getComponentType() == 'G') {
			addGroupChildren(config, parent);
		} else {
			addRecordChildren(config, parent);
		}

	}

	private static void addGroupChildren(ComponentConfig config, Class<?> parent) {
		java.lang.reflect.Field[] var2 = parent.getDeclaredFields();
		int var3 = var2.length;

		int var4;
		Group ga;
		Record ra;
		for (var4 = 0; var4 < var3; ++var4) {
			java.lang.reflect.Field field = var2[var4];
			ga = field.getAnnotation(Group.class);
			ra = field.getAnnotation(Record.class);
			if (ra != null || ga != null) {
				if (ra != null && ga != null) {
					throw new BeanIOConfigurationException(
							"Field '" + field.getName() + "' on class '" + parent.getName()
									+ "' cannot be annotated with both @Record and @Group");
				}

				TypeInfo info = new TypeInfo();
				info.bound = true;
				info.name = field.getName();
				info.type = field.getType();
				info.genericType = field.getGenericType();

				PropertyConfig child;
				try {
					if (ra != null) {
						child = createRecord(info, ra);
					} else {
						child = createGroup(info, ga);
					}
				} catch (IllegalArgumentException var17) {
					IllegalArgumentException ex = var17;
					throw new BeanIOConfigurationException(
							"Invalid annotation for field '" + field.getName() + "' on class '" + parent.getName()
									+ "': " + ex.getMessage(), ex);
				}

				config.add(child);
			}
		}

		Method[] var18 = parent.getDeclaredMethods();
		var3 = var18.length;

		for (var4 = 0; var4 < var3; ++var4) {
			Method method = var18[var4];
			ga = method.getAnnotation(Group.class);
			ra = method.getAnnotation(Record.class);
			if (ra != null || ga != null) {
				if (ra != null && ga != null) {
					throw new BeanIOConfigurationException(
							"Method '" + method.getName() + "' on class '" + parent.getName()
									+ "' cannot be annotated with both @Record and @Group");
				}

				String name = method.getName();
				String getter = null;
				String setter = null;
				Class clazz;
				Type type;
				if (method.getReturnType() != Void.TYPE && method.getParameterTypes().length == 0) {
					getter = name;
					clazz = method.getReturnType();
					type = method.getGenericReturnType();
					if (name.startsWith("get")) {
						name = name.substring(3);
					} else if (name.startsWith("is")) {
						name = name.substring(2);
					}
				} else {
					if (method.getReturnType() != Void.TYPE || method.getParameterTypes().length != 1) {
						throw new BeanIOConfigurationException(
								"Method '" + method.getName() + "' on class '" + parent.getName()
										+ "' is not a valid getter or setter");
					}

					setter = name;
					clazz = method.getParameterTypes()[0];
					type = method.getGenericParameterTypes()[0];
					if (name.startsWith("set")) {
						name = name.substring(3);
					}
				}

				name = Introspector.decapitalize(name);
				TypeInfo info = new TypeInfo();
				info.bound = true;
				info.name = name;
				info.type = clazz;
				info.genericType = type;
				info.getter = getter;
				info.setter = setter;

				PropertyConfig child;
				try {
					if (ra != null) {
						child = createRecord(info, ra);
					} else {
						child = createGroup(info, ga);
					}
				} catch (IllegalArgumentException var16) {
					IllegalArgumentException ex = var16;
					throw new BeanIOConfigurationException(
							"Invalid annotation for method '" + method.getName() + "' on class '" + parent.getName()
									+ "': " + ex.getMessage(), ex);
				}

				config.add(child);
			}
		}

	}

	private static void addRecordChildren(ComponentConfig config, Class<?> parent) {
		java.lang.reflect.Field[] var2 = parent.getDeclaredFields();
		int var3 = var2.length;

		int var4;
		Field fa;
		Segment sa;
		for (var4 = 0; var4 < var3; ++var4) {
			java.lang.reflect.Field field = var2[var4];
			fa = field.getAnnotation(Field.class);
			sa = field.getAnnotation(Segment.class);
			if (fa != null || sa != null) {
				if (fa != null && sa != null) {
					throw new BeanIOConfigurationException(
							"Field '" + field.getName() + "' on class '" + parent.getName()
									+ "' cannot be annotated with both @Field and @Segment");
				}

				TypeInfo info = new TypeInfo();
				info.name = field.getName();
				info.type = field.getType();
				info.genericType = field.getGenericType();

				PropertyConfig child;
				try {
					if (fa != null) {
						child = createField(info, fa);
					} else {
						child = createSegment(info, sa, field.getAnnotation(Fields.class));
					}
				} catch (IllegalArgumentException var17) {
					IllegalArgumentException ex = var17;
					throw new BeanIOConfigurationException(
							"Invalid annotation for field '" + field.getName() + "' on class '" + parent.getName()
									+ "': " + ex.getMessage(), ex);
				}

				config.add(child);
			}
		}

		Method[] var18 = parent.getDeclaredMethods();
		var3 = var18.length;

		for (var4 = 0; var4 < var3; ++var4) {
			Method method = var18[var4];
			fa = method.getAnnotation(Field.class);
			sa = method.getAnnotation(Segment.class);
			if (fa != null || sa != null) {
				if (fa != null && sa != null) {
					throw new BeanIOConfigurationException(
							"Method '" + method.getName() + "' on class '" + parent.getName()
									+ "' cannot be annotated with both @Field and @Segment");
				}

				String name = method.getName();
				String getter = null;
				String setter = null;
				Class clazz;
				Type type;
				if (method.getReturnType() != Void.TYPE && method.getParameterTypes().length == 0) {
					getter = name;
					clazz = method.getReturnType();
					type = method.getGenericReturnType();
					if (name.startsWith("get")) {
						name = name.substring(3);
					} else if (name.startsWith("is")) {
						name = name.substring(2);
					}
				} else {
					if (method.getReturnType() != Void.TYPE || method.getParameterTypes().length != 1) {
						throw new BeanIOConfigurationException(
								"Method '" + method.getName() + "' on class '" + parent.getName()
										+ "' is not a valid getter or setter");
					}

					setter = name;
					clazz = method.getParameterTypes()[0];
					type = method.getGenericParameterTypes()[0];
					if (name.startsWith("set")) {
						name = name.substring(3);
					}
				}

				name = Introspector.decapitalize(name);
				TypeInfo info = new TypeInfo();
				info.name = name;
				info.type = clazz;
				info.genericType = type;
				info.getter = getter;
				info.setter = setter;

				PropertyConfig child;
				try {
					if (fa != null) {
						child = createField(info, fa);
					} else {
						child = createSegment(info, sa, method.getAnnotation(Fields.class));
					}
				} catch (IllegalArgumentException var16) {
					IllegalArgumentException ex = var16;
					throw new BeanIOConfigurationException(
							"Invalid annotation for method '" + method.getName() + "' on class '" + parent.getName()
									+ "': " + ex.getMessage(), ex);
				}

				config.add(child);
			}
		}

	}

	private static GroupConfig createGroup(TypeInfo info, Group group) {
		updateTypeInfo(info, group.type(), group.collection());
		GroupConfig gc = new GroupConfig();
		gc.setName(info.name);
		gc.setType(info.propertyName);
		gc.setOrder(toValue(group.order()));
		gc.setCollection(info.collectionName);
		Integer minOccurs = toValue(group.minOccurs());
		Integer maxOccurs = toUnboundedValue(group.maxOccurs());
		if (maxOccurs == null && info.collectionName == null && info.bound) {
			maxOccurs = 1;
		}

		gc.setMinOccurs(minOccurs);
		gc.setMaxOccurs(maxOccurs);
		gc.setXmlType(group.xmlType().toValue());
		gc.setXmlName(toXmlValue(group.xmlName()));
		gc.setXmlNamespace(toXmlValue(group.xmlNamespace()));
		gc.setXmlPrefix(toXmlValue(group.xmlPrefix()));
		addAllChildren(gc, info.propertyType);
		return gc;
	}

	private static RecordConfig createRecord(TypeInfo info, Record record) {
		updateTypeInfo(info, record.type(), record.collection());
		RecordConfig rc = new RecordConfig();
		rc.setName(info.name);
		String target = toValue(record.value());
		if (target == null) {
			rc.setType(info.propertyName);
		} else {
			rc.setTarget(target);
		}

		rc.setCollection(info.collectionName);
		rc.setOrder(toValue(record.order()));
		Integer minOccurs = toValue(record.minOccurs());
		Integer maxOccurs = toUnboundedValue(record.maxOccurs());
		if (info.bound && maxOccurs == null && info.collectionName == null) {
			maxOccurs = 1;
		}

		rc.setMinOccurs(minOccurs);
		rc.setMaxOccurs(maxOccurs);
		rc.setMinLength(toValue(record.minLength()));
		rc.setMaxLength(toUnboundedValue(record.maxLength()));
		rc.setMinMatchLength(toValue(record.minRidLength()));
		rc.setMaxMatchLength(toUnboundedValue(record.maxRidLength()));
		rc.setXmlType(record.xmlType().toValue());
		rc.setXmlName(toXmlValue(record.xmlName()));
		rc.setXmlNamespace(toXmlValue(record.xmlNamespace()));
		rc.setXmlPrefix(toXmlValue(record.xmlPrefix()));
		Fields fields = info.propertyType.getAnnotation(Fields.class);
		if (fields != null) {
			Field[] var7 = fields.value();
			int var8 = var7.length;

			for (int var9 = 0; var9 < var8; ++var9) {
				Field field = var7[var9];
				rc.add(createField(null, field));
			}
		}

		handleConstructor(rc, info.propertyType);
		addAllChildren(rc, info.propertyType);
		rc.sort(ORDINAL_COMPARATOR);
		return rc;
	}

	private static SegmentConfig createSegment(TypeInfo info, Segment sa, Fields fields) {
		updateTypeInfo(info, sa.type(), sa.collection());
		if (info.propertyType == String.class) {
			throw new IllegalArgumentException("type is undefined");
		} else {
			String target = toValue(sa.value());
			SegmentConfig sc = new SegmentConfig();
			sc.setName(info.name);
			sc.setLabel(toValue(sa.name()));
			if (target == null) {
				sc.setType(info.propertyName);
			} else {
				sc.setTarget(target);
			}

			sc.setCollection(info.collectionName);
			sc.setGetter(toValue(sa.getter()));
			if (sc.getGetter() == null) {
				sc.setGetter(info.getter);
			}

			sc.setSetter(toValue(sa.setter()));
			if (sc.getSetter() == null) {
				sc.setSetter(info.setter);
			}

			sc.setPosition(toValue(sa.at()));
			sc.setUntil(toValue(sa.until()));
			sc.setOrdinal(toValue(sa.ordinal()));
			sc.setMinOccurs(toValue(sa.minOccurs()));
			sc.setMaxOccurs(toUnboundedValue(sa.maxOccurs()));
			sc.setOccursRef(toValue(sa.occursRef()));
			sc.setKey(toValue(sa.key()));
			sc.setLazy(sa.lazy());
			sc.setXmlType(sa.xmlType().toValue());
			sc.setXmlName(toXmlValue(sa.xmlName()));
			sc.setXmlNamespace(toXmlValue(sa.xmlNamespace()));
			sc.setXmlPrefix(toXmlValue(sa.xmlPrefix()));
			sc.setNillable(sa.nillable());
			if (sc.getName() == null) {
				throw new IllegalArgumentException("name is undefined");
			} else {
				Field[] var5;
				int var6;
				int var7;
				Field field;
				if (fields != null) {
					var5 = fields.value();
					var6 = var5.length;

					for (var7 = 0; var7 < var6; ++var7) {
						field = var5[var7];
						sc.add(createField(null, field));
					}
				}

				fields = info.propertyType.getAnnotation(Fields.class);
				if (fields != null) {
					var5 = fields.value();
					var6 = var5.length;

					for (var7 = 0; var7 < var6; ++var7) {
						field = var5[var7];
						sc.add(createField(null, field));
					}
				}

				handleConstructor(sc, info.propertyType);
				addAllChildren(sc, info.propertyType);
				return sc;
			}
		}
	}

	private static FieldConfig createField(TypeInfo info, Field fa) {
		FieldConfig fc = new FieldConfig();
		if (info != null) {
			updateTypeInfo(info, fa.type(), fa.collection());
			fc.setName(info.name);
			fc.setLabel(toValue(fa.name()));
			fc.setType(info.propertyName);
			fc.setCollection(info.collectionName);
			fc.setBound(true);
			fc.setGetter(toValue(fa.getter()));
			if (fc.getGetter() == null) {
				fc.setGetter(info.getter);
			}

			String setter = toValue(fa.setter());
			if (info.carg != null) {
				fc.setSetter("#" + info.carg);
				if (setter != null) {
					throw new BeanIOConfigurationException("setter not allowed");
				}
			} else {
				fc.setSetter(setter);
				if (fc.getSetter() == null) {
					fc.setSetter(info.setter);
				}
			}
		} else {
			fc.setName(toValue(fa.name()));
			fc.setLabel(fc.getName());
			fc.setBound(false);
		}

		if (fc.getName() == null) {
			throw new IllegalArgumentException("name is required");
		} else {
			fc.setLiteral(toValue(fa.literal()));
			fc.setPosition(toValue(fa.at()));
			fc.setUntil(toValue(fa.until()));
			fc.setOrdinal(toValue(fa.ordinal()));
			fc.setRegex(toValue(fa.regex()));
			fc.setFormat(toValue(fa.format()));
			fc.setRequired(fa.required());
			fc.setDefault(toValue(fa.defaultValue()));
			fc.setIdentifier(fa.rid());
			fc.setTrim(fa.trim());
			fc.setLazy(fa.lazy());
			fc.setMinLength(toValue(fa.minLength()));
			fc.setMaxLength(toUnboundedValue(fa.maxLength()));
			fc.setMinOccurs(toValue(fa.minOccurs()));
			fc.setMaxOccurs(toUnboundedValue(fa.maxOccurs()));
			fc.setOccursRef(toValue(fa.occursRef()));
			fc.setLength(toValue(fa.length()));
			if (fa.padding() >= 0 && fa.padding() <= 65535) {
				fc.setPadding((char) fa.padding());
			}

			fc.setJustify(fa.align().toString().toLowerCase());
			fc.setKeepPadding(fa.keepPadding());
			fc.setLenientPadding(fa.lenientPadding());
			fc.setTypeHandler(toValue(fa.handlerName()));
			Class<?> handler = toValue(fa.handlerClass());
			if (handler != null && fc.getTypeHandler() == null) {
				fc.setTypeHandler(fa.handlerClass().getName());
			}

			fc.setXmlType(fa.xmlType().toValue());
			fc.setXmlName(toXmlValue(fa.xmlName()));
			fc.setXmlNamespace(toXmlValue(fa.xmlNamespace()));
			fc.setXmlPrefix(toXmlValue(fa.xmlPrefix()));
			fc.setNillable(fa.nillable());
			return fc;
		}
	}

	/**
	 * TODO: control evaluation into parameterized collection fields.
	 */
	private static void updateTypeInfo(TypeInfo info, Class<?> annotatedType, Class<?> annotatedCollection) {
		annotatedType = toValue(annotatedType);
		String propertyName = null;
		String collectionName = null;
		Class<?> propertyType = info.type;
		if (propertyType.isArray()) {
			if (annotatedType != null) {
				propertyType = annotatedType;
			} else {
				propertyType = propertyType.getComponentType();
				if (propertyType.isPrimitive()) {
					propertyType = TypeUtil.toWrapperClass(propertyType);
				}
			}

			collectionName = "array";
//		} else {
//        TODO: FIX -> added check to ensure that property type not equal to annotated type
		} else if (!java.util.Objects.equals(propertyType, annotatedType)) {
			Class collectionType;
			ParameterizedType pt;
			if (Map.class.isAssignableFrom(propertyType)) {
				collectionType = toValue(annotatedCollection);
				if (collectionType == null) {
					collectionType = propertyType;
					propertyType = null;
				}

				if (annotatedType != null) {
					propertyType = annotatedType;
				} else {
					if (info.genericType instanceof ParameterizedType) {
						pt = (ParameterizedType) info.genericType;
						if (pt.getActualTypeArguments().length > 1) {
							propertyType = (Class) pt.getActualTypeArguments()[1];
						}
					}

					if (propertyType == null) {
						propertyType = String.class;
					}
				}

				collectionName = collectionType.getName();
			} else if (Collection.class.isAssignableFrom(propertyType)) {
				collectionType = toValue(annotatedCollection);
				if (collectionType == null) {
					collectionType = propertyType;
					propertyType = null;
				}

				if (annotatedType != null) {
					propertyType = annotatedType;
				} else {
					if (info.genericType instanceof ParameterizedType) {
						pt = (ParameterizedType) info.genericType;
						if (pt.getActualTypeArguments().length > 0) {
							propertyType = (Class) pt.getActualTypeArguments()[0];
						}
					}

					if (propertyType == null) {
						propertyType = String.class;
					}
				}

				collectionName = collectionType.getName();
			} else if (annotatedType != null) {
				propertyType = annotatedType;
			} else if (propertyType.isPrimitive()) {
				propertyType = TypeUtil.toWrapperClass(propertyType);
			}
		}

		if (propertyName == null) {
			propertyName = propertyType.getName();
		}

		info.propertyType = propertyType;
		info.propertyName = propertyName;
		info.collectionName = collectionName;
	}

	private static Class<?> toValue(Class<?> type) {
		return Void.class == type ? null : type;
	}

	private static Integer toValue(int n) {
		return n == Integer.MIN_VALUE ? null : n;
	}

	private static Integer toUnboundedValue(int n) {
		Integer val = toValue(n);
		if (val == null) {
			return null;
		} else {
			return val.compareTo(0) < 0 ? Integer.MAX_VALUE : val;
		}
	}

	private static String toValue(String s) {
		return "".equals(s) ? null : s;
	}

	private static String toXmlValue(String s) {
		return "{undefined}".equals(s) ? null : s;
	}

	private static class TypeInfo {

		boolean bound;
		Integer carg;
		String name;
		Class<?> type;
		Type genericType;
		String propertyName;
		String collectionName;
		Class<?> propertyType;
		String getter;
		String setter;

		private TypeInfo() {
		}
	}
}