package uk.co.bluegecko.common.model.invoice;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.lang.Nullable;

@FieldNameConstants(asEnum = true)
@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

	String name;
	@Nullable
	String company;
	String building;
	String street;
	@Nullable
	String locality;
	String town;
	@Nullable
	String state;
	String postcode;
	@Nullable
	String country;

}