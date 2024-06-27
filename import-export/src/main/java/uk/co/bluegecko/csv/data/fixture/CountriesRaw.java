package uk.co.bluegecko.csv.data.fixture;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import uk.co.bluegecko.common.model.country.Country;

public class CountriesRaw {

	public static <R extends Country> List<R> from(Function<String[], R> convert) {
		return Arrays.stream(countries()).map(convert).toList();
	}

	public static <R extends Country> R from(Function<String[], R> convert, int index) {
		return convert.apply(countries()[index]);
	}

	public static String[][] countries() {
		return new String[][]{
				new String[]{"1", "AD", "Andorra", "Andorra", "376", "Europe", "Andorra la Vella", "EUR", "ca"},
				new String[]{"2", "AE", "United Arab Emirates", "دولة الإمارات العربية المتحدة", "971", "Asia",
						"Abu Dhabi", "AED", "ar"},
				new String[]{"3", "AF", "Afghanistan", "افغانستان", "93", "Asia", "Kabul", "AFN", "ps,uz,tk"},
				new String[]{"4", "AG", "Antigua and Barbuda", "Antigua and Barbuda", "1268", "North America",
						"Saint John's", "XCD", "en"},
				new String[]{"5", "AI", "Anguilla", "Anguilla", "1264", "North America", "The Valley", "XCD", "en"},
				new String[]{"6", "AL", "Albania", "Shqipëria", "355", "Europe", "Tirana", "ALL", "sq"},
				new String[]{"7", "AM", "Armenia", "Հայաստան", "374", "Asia", "Yerevan", "AMD", "hy,ru"},
				new String[]{"8", "AO", "Angola", "Angola", "244", "Africa", "Luanda", "AOA", "pt"},
				new String[]{"9", "AQ", "Antarctica", "Antarctica", "672", "Antarctica", "", "", ""},
				new String[]{"10", "AR", "Argentina", "Argentina", "54", "South America", "Buenos Aires", "ARS",
						"es,gn"},
				new String[]{"11", "AS", "American Samoa", "American Samoa", "1684", "Oceania", "Pago Pago", "USD",
						"en,sm"},
				new String[]{"12", "AT", "Austria", "Österreich", "43", "Europe", "Vienna", "EUR", "de"},
				new String[]{"13", "AU", "Australia", "Australia", "61", "Oceania", "Canberra", "AUD", "en"},
				new String[]{"14", "AW", "Aruba", "Aruba", "297", "North America", "Oranjestad", "AWG", "nl,pa"},
				new String[]{"15", "AX", "Aland", "Åland", "358", "Europe", "Mariehamn", "EUR", "sv"},
				new String[]{"16", "AZ", "Azerbaijan", "Azərbaycan", "994", "Asia", "Baku", "AZN", "az"},
				new String[]{"17", "BA", "Bosnia and Herzegovina", "Bosna i Hercegovina", "387", "Europe", "Sarajevo",
						"BAM", "bs,hr,sr"},
				new String[]{"18", "BB", "Barbados", "Barbados", "1246", "North America", "Bridgetown", "BBD", "en"},
				new String[]{"19", "BD", "Bangladesh", "Bangladesh", "880", "Asia", "Dhaka", "BDT", "bn"},
				new String[]{"20", "BE", "Belgium", "België", "32", "Europe", "Brussels", "EUR", "nl,fr,de"},
				new String[]{"21", "BF", "Burkina Faso", "Burkina Faso", "226", "Africa", "Ouagadougou", "XOF",
						"fr,ff"},
				new String[]{"22", "BG", "Bulgaria", "България", "359", "Europe", "Sofia", "BGN", "bg"},
				new String[]{"23", "BH", "Bahrain", "‏البحرين", "973", "Asia", "Manama", "BHD", "ar"},
				new String[]{"24", "BI", "Burundi", "Burundi", "257", "Africa", "Bujumbura", "BIF", "fr,rn"},
				new String[]{"25", "BJ", "Benin", "Bénin", "229", "Africa", "Porto-Novo", "XOF", "fr"},
				new String[]{"26", "BL", "Saint Barthelemy", "Saint-Barthélemy", "590", "North America", "Gustavia",
						"EUR", "fr"},
				new String[]{"27", "BM", "Bermuda", "Bermuda", "1441", "North America", "Hamilton", "BMD", "en"},
				new String[]{"28", "BN", "Brunei", "Negara Brunei Darussalam", "673", "Asia", "Bandar Seri Begawan",
						"BND", "ms"},
				new String[]{"29", "BO", "Bolivia", "Bolivia", "591", "South America", "Sucre", "BOB,BOV", "es,ay,qu"},
				new String[]{"30", "BQ", "Bonaire", "Bonaire", "5997", "North America", "Kralendijk", "USD", "nl"},
				new String[]{"31", "BR", "Brazil", "Brasil", "55", "South America", "Brasília", "BRL", "pt"},
				new String[]{"32", "BS", "Bahamas", "Bahamas", "1242", "North America", "Nassau", "BSD", "en"},
				new String[]{"33", "BT", "Bhutan", "ʼbrug-yul", "975", "Asia", "Thimphu", "BTN,INR", "dz"},
				new String[]{"34", "BV", "Bouvet Island", "Bouvetøya", "47", "Antarctica", "", "NOK", "no,nb,nn"},
				new String[]{"35", "BW", "Botswana", "Botswana", "267", "Africa", "Gaborone", "BWP", "en,tn"},
				new String[]{"36", "BY", "Belarus", "Белару́сь", "375", "Europe", "Minsk", "BYN", "be,ru"},
				new String[]{"37", "BZ", "Belize", "Belize", "501", "North America", "Belmopan", "BZD", "en,es"},
				new String[]{"38", "CA", "Canada", "Canada", "1", "North America", "Ottawa", "CAD", "en,fr"},
				new String[]{"39", "CC", "Cocos (Keeling) Islands", "Cocos (Keeling) Islands", "61", "Asia",
						"West Island", "AUD", "en"},
				new String[]{"40", "CD", "Democratic Republic of the Congo", "République démocratique du Congo", "243",
						"Africa", "Kinshasa", "CDF", "fr,ln,kg,sw,lu"},
				new String[]{"41", "CF", "Central African Republic", "Ködörösêse tî Bêafrîka", "236", "Africa",
						"Bangui", "XAF", "fr,sg"},
				new String[]{"42", "CG", "Republic of the Congo", "République du Congo", "242", "Africa", "Brazzaville",
						"XAF", "fr,ln"},
				new String[]{"43", "CH", "Switzerland", "Schweiz", "41", "Europe", "Bern", "CHE,CHF,CHW", "de,fr,it"},
				new String[]{"44", "CI", "Ivory Coast", "Côte d'Ivoire", "225", "Africa", "Yamoussoukro", "XOF", "fr"},
				new String[]{"45", "CK", "Cook Islands", "Cook Islands", "682", "Oceania", "Avarua", "NZD", "en"},
				new String[]{"46", "CL", "Chile", "Chile", "56", "South America", "Santiago", "CLF,CLP", "es"},
				new String[]{"47", "CM", "Cameroon", "Cameroon", "237", "Africa", "Yaoundé", "XAF", "en,fr"},
				new String[]{"48", "CN", "China", "中国", "86", "Asia", "Beijing", "CNY", "zh"},
				new String[]{"49", "CO", "Colombia", "Colombia", "57", "South America", "Bogotá", "COP", "es"},
				new String[]{"50", "CR", "Costa Rica", "Costa Rica", "506", "North America", "San José", "CRC", "es"},
				new String[]{"51", "CU", "Cuba", "Cuba", "53", "North America", "Havana", "CUC,CUP", "es"},
				new String[]{"52", "CV", "Cape Verde", "Cabo Verde", "238", "Africa", "Praia", "CVE", "pt"},
				new String[]{"53", "CW", "Curacao", "Curaçao", "5999", "North America", "Willemstad", "ANG",
						"nl,pa,en"},
				new String[]{"54", "CX", "Christmas Island", "Christmas Island", "61", "Asia", "Flying Fish Cove",
						"AUD", "en"},
				new String[]{"55", "CY", "Cyprus", "Κύπρος", "357", "Europe", "Nicosia", "EUR", "el,tr,hy"},
				new String[]{"56", "CZ", "Czech Republic", "Česká republika", "420", "Europe", "Prague", "CZK", "cs"},
				new String[]{"57", "DE", "Germany", "Deutschland", "49", "Europe", "Berlin", "EUR", "de"},
				new String[]{"58", "DJ", "Djibouti", "Djibouti", "253", "Africa", "Djibouti", "DJF", "fr,ar"},
				new String[]{"59", "DK", "Denmark", "Danmark", "45", "Europe", "Copenhagen", "DKK", "da"},
				new String[]{"60", "DM", "Dominica", "Dominica", "1767", "North America", "Roseau", "XCD", "en"},
				new String[]{"61", "DO", "Dominican Republic", "República Dominicana", "1809,1829,1849",
						"North America", "Santo Domingo", "DOP", "es"},
				new String[]{"62", "DZ", "Algeria", "الجزائر", "213", "Africa", "Algiers", "DZD", "ar"},
				new String[]{"63", "EC", "Ecuador", "Ecuador", "593", "South America", "Quito", "USD", "es"},
				new String[]{"64", "EE", "Estonia", "Eesti", "372", "Europe", "Tallinn", "EUR", "et"},
				new String[]{"65", "EG", "Egypt", "مصر‎", "20", "Africa", "Cairo", "EGP", "ar"},
				new String[]{"66", "EH", "Western Sahara", "الصحراء الغربية", "212", "Africa", "El Aaiún",
						"MAD,DZD,MRU", "es"},
				new String[]{"67", "ER", "Eritrea", "ኤርትራ", "291", "Africa", "Asmara", "ERN", "ti,ar,en"},
				new String[]{"68", "ES", "Spain", "España", "34", "Europe", "Madrid", "EUR", "es,eu,ca,gl,oc"},
				new String[]{"69", "ET", "Ethiopia", "ኢትዮጵያ", "251", "Africa", "Addis Ababa", "ETB", "am"},
				new String[]{"70", "FI", "Finland", "Suomi", "358", "Europe", "Helsinki", "EUR", "fi,sv"},
				new String[]{"71", "FJ", "Fiji", "Fiji", "679", "Oceania", "Suva", "FJD", "en,fj,hi,ur"},
				new String[]{"72", "FK", "Falkland Islands", "Falkland Islands", "500", "South America", "Stanley",
						"FKP", "en"},
				new String[]{"73", "FM", "Micronesia", "Micronesia", "691", "Oceania", "Palikir", "USD", "en"},
				new String[]{"74", "FO", "Faroe Islands", "Føroyar", "298", "Europe", "Tórshavn", "DKK", "fo"},
				new String[]{"75", "FR", "France", "France", "33", "Europe", "Paris", "EUR", "fr"},
				new String[]{"76", "GA", "Gabon", "Gabon", "241", "Africa", "Libreville", "XAF", "fr"},
				new String[]{"77", "GB", "United Kingdom", "United Kingdom", "44", "Europe", "London", "GBP", "en"},
				new String[]{"78", "GD", "Grenada", "Grenada", "1473", "North America", "St. George's", "XCD", "en"},
				new String[]{"79", "GE", "Georgia", "საქართველო", "995", "Asia", "Tbilisi", "GEL", "ka"},
				new String[]{"80", "GF", "French Guiana", "Guyane française", "594", "South America", "Cayenne", "EUR",
						"fr"},
				new String[]{"81", "GG", "Guernsey", "Guernsey", "44", "Europe", "St. Peter Port", "GBP", "en,fr"},
				new String[]{"82", "GH", "Ghana", "Ghana", "233", "Africa", "Accra", "GHS", "en"},
				new String[]{"83", "GI", "Gibraltar", "Gibraltar", "350", "Europe", "Gibraltar", "GIP", "en"},
				new String[]{"84", "GL", "Greenland", "Kalaallit Nunaat", "299", "North America", "Nuuk", "DKK", "kl"},
				new String[]{"85", "GM", "Gambia", "Gambia", "220", "Africa", "Banjul", "GMD", "en"},
				new String[]{"86", "GN", "Guinea", "Guinée", "224", "Africa", "Conakry", "GNF", "fr,ff"},
				new String[]{"87", "GP", "Guadeloupe", "Guadeloupe", "590", "North America", "Basse-Terre", "EUR",
						"fr"},
				new String[]{"88", "GQ", "Equatorial Guinea", "Guinea Ecuatorial", "240", "Africa", "Malabo", "XAF",
						"es,fr"},
				new String[]{"89", "GR", "Greece", "Ελλάδα", "30", "Europe", "Athens", "EUR", "el"},
				new String[]{"90", "GS", "South Georgia and the South Sandwich Islands", "South Georgia", "500",
						"Antarctica", "King Edward Point", "GBP", "en"},
				new String[]{"91", "GT", "Guatemala", "Guatemala", "502", "North America", "Guatemala City", "GTQ",
						"es"},
				new String[]{"92", "GU", "Guam", "Guam", "1671", "Oceania", "Hagåtña", "USD", "en,ch,es"},
				new String[]{"93", "GW", "Guinea-Bissau", "Guiné-Bissau", "245", "Africa", "Bissau", "XOF", "pt"},
				new String[]{"94", "GY", "Guyana", "Guyana", "592", "South America", "Georgetown", "GYD", "en"},
				new String[]{"95", "HK", "Hong Kong", "香港", "852", "Asia", "City of Victoria", "HKD", "zh,en"},
				new String[]{"96", "HM", "Heard Island and McDonald Islands", "Heard Island and McDonald Islands", "61",
						"Antarctica", "", "AUD", "en"},
				new String[]{"97", "HN", "Honduras", "Honduras", "504", "North America", "Tegucigalpa", "HNL", "es"},
				new String[]{"98", "HR", "Croatia", "Hrvatska", "385", "Europe", "Zagreb", "EUR", "hr"},
				new String[]{"99", "HT", "Haiti", "Haïti", "509", "North America", "Port-au-Prince", "HTG,USD",
						"fr,ht"},
				new String[]{"100", "HU", "Hungary", "Magyarország", "36", "Europe", "Budapest", "HUF", "hu"},
				new String[]{"101", "ID", "Indonesia", "Indonesia", "62", "Asia", "Jakarta", "IDR", "id"},
				new String[]{"102", "IE", "Ireland", "Éire", "353", "Europe", "Dublin", "EUR", "ga,en"},
				new String[]{"103", "IL", "Israel", "יִשְׂרָאֵל", "972", "Asia", "Jerusalem", "ILS", "he,ar"},
				new String[]{"104", "IM", "Isle of Man", "Isle of Man", "44", "Europe", "Douglas", "GBP", "en,gv"},
				new String[]{"105", "IN", "India", "भारत", "91", "Asia", "New Delhi", "INR", "hi,en"},
				new String[]{"106", "IO", "British Indian Ocean Territory", "British Indian Ocean Territory", "246",
						"Asia", "Diego Garcia", "USD", "en"},
				new String[]{"107", "IQ", "Iraq", "العراق", "964", "Asia", "Baghdad", "IQD", "ar,ku"},
				new String[]{"108", "IR", "Iran", "ایران", "98", "Asia", "Tehran", "IRR", "fa"},
				new String[]{"109", "IS", "Iceland", "Ísland", "354", "Europe", "Reykjavik", "ISK", "is"},
				new String[]{"110", "IT", "Italy", "Italia", "39", "Europe", "Rome", "EUR", "it"},
				new String[]{"111", "JE", "Jersey", "Jersey", "44", "Europe", "Saint Helier", "GBP", "en,fr"},
				new String[]{"112", "JM", "Jamaica", "Jamaica", "1876", "North America", "Kingston", "JMD", "en"},
				new String[]{"113", "JO", "Jordan", "الأردن", "962", "Asia", "Amman", "JOD", "ar"},
				new String[]{"114", "JP", "Japan", "日本", "81", "Asia", "Tokyo", "JPY", "ja"},
				new String[]{"115", "KE", "Kenya", "Kenya", "254", "Africa", "Nairobi", "KES", "en,sw"},
				new String[]{"116", "KG", "Kyrgyzstan", "Кыргызстан", "996", "Asia", "Bishkek", "KGS", "ky,ru"},
				new String[]{"117", "KH", "Cambodia", "Kâmpŭchéa", "855", "Asia", "Phnom Penh", "KHR", "km"},
				new String[]{"118", "KI", "Kiribati", "Kiribati", "686", "Oceania", "South Tarawa", "AUD", "en"},
				new String[]{"119", "KM", "Comoros", "Komori", "269", "Africa", "Moroni", "KMF", "ar,fr"},
				new String[]{"120", "KN", "Saint Kitts and Nevis", "Saint Kitts and Nevis", "1869", "North America",
						"Basseterre", "XCD", "en"},
				new String[]{"121", "KP", "North Korea", "북한", "850", "Asia", "Pyongyang", "KPW", "ko"},
				new String[]{"122", "KR", "South Korea", "대한민국", "82", "Asia", "Seoul", "KRW", "ko"},
				new String[]{"123", "KW", "Kuwait", "الكويت", "965", "Asia", "Kuwait City", "KWD", "ar"},
				new String[]{"124", "KY", "Cayman Islands", "Cayman Islands", "1345", "North America", "George Town",
						"KYD", "en"},
				new String[]{"125", "KZ", "Kazakhstan", "Қазақстан", "7", "Asia", "Astana", "KZT", "kk,ru"},
				new String[]{"126", "LA", "Laos", "ສປປລາວ", "856", "Asia", "Vientiane", "LAK", "lo"},
				new String[]{"127", "LB", "Lebanon", "لبنان", "961", "Asia", "Beirut", "LBP", "ar,fr"},
				new String[]{"128", "LC", "Saint Lucia", "Saint Lucia", "1758", "North America", "Castries", "XCD",
						"en"},
				new String[]{"129", "LI", "Liechtenstein", "Liechtenstein", "423", "Europe", "Vaduz", "CHF", "de"},
				new String[]{"130", "LK", "Sri Lanka", "śrī laṃkāva", "94", "Asia", "Colombo", "LKR", "si,ta"},
				new String[]{"131", "LR", "Liberia", "Liberia", "231", "Africa", "Monrovia", "LRD", "en"},
				new String[]{"132", "LS", "Lesotho", "Lesotho", "266", "Africa", "Maseru", "LSL,ZAR", "en,st"},
				new String[]{"133", "LT", "Lithuania", "Lietuva", "370", "Europe", "Vilnius", "EUR", "lt"},
				new String[]{"134", "LU", "Luxembourg", "Luxembourg", "352", "Europe", "Luxembourg", "EUR", "fr,de,lb"},
				new String[]{"135", "LV", "Latvia", "Latvija", "371", "Europe", "Riga", "EUR", "lv"},
				new String[]{"136", "LY", "Libya", "‏ليبيا", "218", "Africa", "Tripoli", "LYD", "ar"},
				new String[]{"137", "MA", "Morocco", "المغرب", "212", "Africa", "Rabat", "MAD", "ar"},
				new String[]{"138", "MC", "Monaco", "Monaco", "377", "Europe", "Monaco", "EUR", "fr"},
				new String[]{"139", "MD", "Moldova", "Moldova", "373", "Europe", "Chișinău", "MDL", "ro"},
				new String[]{"140", "ME", "Montenegro", "Црна Гора", "382", "Europe", "Podgorica", "EUR",
						"sr,bs,sq,hr"},
				new String[]{"141", "MF", "Saint Martin", "Saint-Martin", "590", "North America", "Marigot", "EUR",
						"en,fr,nl"},
				new String[]{"142", "MG", "Madagascar", "Madagasikara", "261", "Africa", "Antananarivo", "MGA",
						"fr,mg"},
				new String[]{"143", "MH", "Marshall Islands", "M̧ajeļ", "692", "Oceania", "Majuro", "USD", "en,mh"},
				new String[]{"144", "MK", "North Macedonia", "Северна Македонија", "389", "Europe", "Skopje", "MKD",
						"mk"},
				new String[]{"145", "ML", "Mali", "Mali", "223", "Africa", "Bamako", "XOF", "fr"},
				new String[]{"146", "MM", "Myanmar (Burma)", "မြန်မာ", "95", "Asia", "Naypyidaw", "MMK", "my"},
				new String[]{"147", "MN", "Mongolia", "Монгол улс", "976", "Asia", "Ulan Bator", "MNT", "mn"},
				new String[]{"148", "MO", "Macao", "澳門", "853", "Asia", "", "MOP", "zh,pt"},
				new String[]{"149", "MP", "Northern Mariana Islands", "Northern Mariana Islands", "1670", "Oceania",
						"Saipan", "USD", "en,ch"},
				new String[]{"150", "MQ", "Martinique", "Martinique", "596", "North America", "Fort-de-France", "EUR",
						"fr"},
				new String[]{"151", "MR", "Mauritania", "موريتانيا", "222", "Africa", "Nouakchott", "MRU", "ar"},
				new String[]{"152", "MS", "Montserrat", "Montserrat", "1664", "North America", "Plymouth", "XCD", "en"},
				new String[]{"153", "MT", "Malta", "Malta", "356", "Europe", "Valletta", "EUR", "mt,en"},
				new String[]{"154", "MU", "Mauritius", "Maurice", "230", "Africa", "Port Louis", "MUR", "en"},
				new String[]{"155", "MV", "Maldives", "Maldives", "960", "Asia", "Malé", "MVR", "dv"},
				new String[]{"156", "MW", "Malawi", "Malawi", "265", "Africa", "Lilongwe", "MWK", "en,ny"},
				new String[]{"157", "MX", "Mexico", "México", "52", "North America", "Mexico City", "MXN", "es"},
				new String[]{"158", "MY", "Malaysia", "Malaysia", "60", "Asia", "Kuala Lumpur", "MYR", "ms"},
				new String[]{"159", "MZ", "Mozambique", "Moçambique", "258", "Africa", "Maputo", "MZN", "pt"},
				new String[]{"160", "NA", "Namibia", "Namibia", "264", "Africa", "Windhoek", "NAD,ZAR", "en,af"},
				new String[]{"161", "NC", "New Caledonia", "Nouvelle-Calédonie", "687", "Oceania", "Nouméa", "XPF",
						"fr"},
				new String[]{"162", "NE", "Niger", "Niger", "227", "Africa", "Niamey", "XOF", "fr"},
				new String[]{"163", "NF", "Norfolk Island", "Norfolk Island", "672", "Oceania", "Kingston", "AUD",
						"en"},
				new String[]{"164", "NG", "Nigeria", "Nigeria", "234", "Africa", "Abuja", "NGN", "en"},
				new String[]{"165", "NI", "Nicaragua", "Nicaragua", "505", "North America", "Managua", "NIO", "es"},
				new String[]{"166", "NL", "Netherlands", "Nederland", "31", "Europe", "Amsterdam", "EUR", "nl"},
				new String[]{"167", "NO", "Norway", "Norge", "47", "Europe", "Oslo", "NOK", "no,nb,nn"},
				new String[]{"168", "NP", "Nepal", "नेपाल", "977", "Asia", "Kathmandu", "NPR", "ne"},
				new String[]{"169", "NR", "Nauru", "Nauru", "674", "Oceania", "Yaren", "AUD", "en,na"},
				new String[]{"170", "NU", "Niue", "Niuē", "683", "Oceania", "Alofi", "NZD", "en"},
				new String[]{"171", "NZ", "New Zealand", "New Zealand", "64", "Oceania", "Wellington", "NZD", "en,mi"},
				new String[]{"172", "OM", "Oman", "عمان", "968", "Asia", "Muscat", "OMR", "ar"},
				new String[]{"173", "PA", "Panama", "Panamá", "507", "North America", "Panama City", "PAB,USD", "es"},
				new String[]{"174", "PE", "Peru", "Perú", "51", "South America", "Lima", "PEN", "es"},
				new String[]{"175", "PF", "French Polynesia", "Polynésie française", "689", "Oceania", "Papeetē", "XPF",
						"fr"},
				new String[]{"176", "PG", "Papua New Guinea", "Papua Niugini", "675", "Oceania", "Port Moresby", "PGK",
						"en"},
				new String[]{"177", "PH", "Philippines", "Pilipinas", "63", "Asia", "Manila", "PHP", "en"},
				new String[]{"178", "PK", "Pakistan", "Pakistan", "92", "Asia", "Islamabad", "PKR", "en,ur"},
				new String[]{"179", "PL", "Poland", "Polska", "48", "Europe", "Warsaw", "PLN", "pl"},
				new String[]{"180", "PM", "Saint Pierre and Miquelon", "Saint-Pierre-et-Miquelon", "508",
						"North America", "Saint-Pierre", "EUR", "fr"},
				new String[]{"181", "PN", "Pitcairn Islands", "Pitcairn Islands", "64", "Oceania", "Adamstown", "NZD",
						"en"},
				new String[]{"182", "PR", "Puerto Rico", "Puerto Rico", "1787,1939", "North America", "San Juan", "USD",
						"es,en"},
				new String[]{"183", "PS", "Palestine", "فلسطين", "970", "Asia", "Ramallah", "ILS", "ar"},
				new String[]{"184", "PT", "Portugal", "Portugal", "351", "Europe", "Lisbon", "EUR", "pt"},
				new String[]{"185", "PW", "Palau", "Palau", "680", "Oceania", "Ngerulmud", "USD", "en"},
				new String[]{"186", "PY", "Paraguay", "Paraguay", "595", "South America", "Asunción", "PYG", "es,gn"},
				new String[]{"187", "QA", "Qatar", "قطر", "974", "Asia", "Doha", "QAR", "ar"},
				new String[]{"188", "RE", "Reunion", "La Réunion", "262", "Africa", "Saint-Denis", "EUR", "fr"},
				new String[]{"189", "RO", "Romania", "România", "40", "Europe", "Bucharest", "RON", "ro"},
				new String[]{"190", "RS", "Serbia", "Србија", "381", "Europe", "Belgrade", "RSD", "sr"},
				new String[]{"191", "RU", "Russia", "Россия", "7", "Asia", "Moscow", "RUB", "ru"},
				new String[]{"192", "RW", "Rwanda", "Rwanda", "250", "Africa", "Kigali", "RWF", "rw,en,fr"},
				new String[]{"193", "SA", "Saudi Arabia", "العربية السعودية", "966", "Asia", "Riyadh", "SAR", "ar"},
				new String[]{"194", "SB", "Solomon Islands", "Solomon Islands", "677", "Oceania", "Honiara", "SBD",
						"en"},
				new String[]{"195", "SC", "Seychelles", "Seychelles", "248", "Africa", "Victoria", "SCR", "fr,en"},
				new String[]{"196", "SD", "Sudan", "السودان", "249", "Africa", "Khartoum", "SDG", "ar,en"},
				new String[]{"197", "SE", "Sweden", "Sverige", "46", "Europe", "Stockholm", "SEK", "sv"},
				new String[]{"198", "SG", "Singapore", "Singapore", "65", "Asia", "Singapore", "SGD", "en,ms,ta,zh"},
				new String[]{"199", "SH", "Saint Helena", "Saint Helena", "290", "Africa", "Jamestown", "SHP", "en"},
				new String[]{"200", "SI", "Slovenia", "Slovenija", "386", "Europe", "Ljubljana", "EUR", "sl"},
				new String[]{"201", "SJ", "Svalbard and Jan Mayen", "Svalbard og Jan Mayen", "4779", "Europe",
						"Longyearbyen", "NOK", "no"},
				new String[]{"202", "SK", "Slovakia", "Slovensko", "421", "Europe", "Bratislava", "EUR", "sk"},
				new String[]{"203", "SL", "Sierra Leone", "Sierra Leone", "232", "Africa", "Freetown", "SLL", "en"},
				new String[]{"204", "SM", "San Marino", "San Marino", "378", "Europe", "City of San Marino", "EUR",
						"it"},
				new String[]{"205", "SN", "Senegal", "Sénégal", "221", "Africa", "Dakar", "XOF", "fr"},
				new String[]{"206", "SO", "Somalia", "Soomaaliya", "252", "Africa", "Mogadishu", "SOS", "so,ar"},
				new String[]{"207", "SR", "Suriname", "Suriname", "597", "South America", "Paramaribo", "SRD", "nl"},
				new String[]{"208", "SS", "South Sudan", "South Sudan", "211", "Africa", "Juba", "SSP", "en"},
				new String[]{"209", "ST", "Sao Tome and Principe", "São Tomé e Príncipe", "239", "Africa", "São Tomé",
						"STN", "pt"},
				new String[]{"210", "SV", "El Salvador", "El Salvador", "503", "North America", "San Salvador",
						"SVC,USD", "es"},
				new String[]{"211", "SX", "Sint Maarten", "Sint Maarten", "1721", "North America", "Philipsburg", "ANG",
						"nl,en"},
				new String[]{"212", "SY", "Syria", "سوريا", "963", "Asia", "Damascus", "SYP", "ar"},
				new String[]{"213", "SZ", "Eswatini", "Eswatini", "268", "Africa", "Lobamba", "SZL", "en,ss"},
				new String[]{"214", "TC", "Turks and Caicos Islands", "Turks and Caicos Islands", "1649",
						"North America", "Cockburn Town", "USD", "en"},
				new String[]{"215", "TD", "Chad", "Tchad", "235", "Africa", "N'Djamena", "XAF", "fr,ar"},
				new String[]{"216", "TF", "French Southern Territories",
						"Territoire des Terres australes et antarctiques fr", "262", "Antarctica", "Port-aux-Français",
						"EUR", "fr"},
				new String[]{"217", "TG", "Togo", "Togo", "228", "Africa", "Lomé", "XOF", "fr"},
				new String[]{"218", "TH", "Thailand", "ประเทศไทย", "66", "Asia", "Bangkok", "THB", "th"},
				new String[]{"219", "TJ", "Tajikistan", "Тоҷикистон", "992", "Asia", "Dushanbe", "TJS", "tg,ru"},
				new String[]{"220", "TK", "Tokelau", "Tokelau", "690", "Oceania", "Fakaofo", "NZD", "en"},
				new String[]{"221", "TL", "East Timor", "Timor-Leste", "670", "Oceania", "Dili", "USD", "pt"},
				new String[]{"222", "TM", "Turkmenistan", "Türkmenistan", "993", "Asia", "Ashgabat", "TMT", "tk,ru"},
				new String[]{"223", "TN", "Tunisia", "تونس", "216", "Africa", "Tunis", "TND", "ar"},
				new String[]{"224", "TO", "Tonga", "Tonga", "676", "Oceania", "Nuku'alofa", "TOP", "en,to"},
				new String[]{"225", "TR", "Turkey", "Türkiye", "90", "Asia", "Ankara", "TRY", "tr"},
				new String[]{"226", "TT", "Trinidad and Tobago", "Trinidad and Tobago", "1868", "North America",
						"Port of Spain", "TTD", "en"},
				new String[]{"227", "TV", "Tuvalu", "Tuvalu", "688", "Oceania", "Funafuti", "AUD", "en"},
				new String[]{"228", "TW", "Taiwan", "臺灣", "886", "Asia", "Taipei", "TWD", "zh"},
				new String[]{"229", "TZ", "Tanzania", "Tanzania", "255", "Africa", "Dodoma", "TZS", "sw,en"},
				new String[]{"230", "UA", "Ukraine", "Україна", "380", "Europe", "Kyiv", "UAH", "uk"},
				new String[]{"231", "UG", "Uganda", "Uganda", "256", "Africa", "Kampala", "UGX", "en,sw"},
				new String[]{"232", "UM", "U.S. Minor Outlying Islands", "United States Minor Outlying Islands", "1",
						"Oceania", "", "USD", "en"},
				new String[]{"233", "US", "United States", "United States", "1", "North America", "Washington D.C.",
						"USD,USN,USS", "en"},
				new String[]{"234", "UY", "Uruguay", "Uruguay", "598", "South America", "Montevideo", "UYI,UYU", "es"},
				new String[]{"235", "UZ", "Uzbekistan", "O'zbekiston", "998", "Asia", "Tashkent", "UZS", "uz,ru"},
				new String[]{"236", "VA", "Vatican City", "Vaticano", "379", "Europe", "Vatican City", "EUR", "it,la"},
				new String[]{"237", "VC", "Saint Vincent and the Grenadines", "Saint Vincent and the Grenadines",
						"1784", "North America", "Kingstown", "XCD", "en"},
				new String[]{"238", "VE", "Venezuela", "Venezuela", "58", "South America", "Caracas", "VES", "es"},
				new String[]{"239", "VG", "British Virgin Islands", "British Virgin Islands", "1284", "North America",
						"Road Town", "USD", "en"},
				new String[]{"240", "VI", "U.S. Virgin Islands", "United States Virgin Islands", "1340",
						"North America", "Charlotte Amalie", "USD", "en"},
				new String[]{"241", "VN", "Vietnam", "Việt Nam", "84", "Asia", "Hanoi", "VND", "vi"},
				new String[]{"242", "VU", "Vanuatu", "Vanuatu", "678", "Oceania", "Port Vila", "VUV", "bi,en,fr"},
				new String[]{"243", "WF", "Wallis and Futuna", "Wallis et Futuna", "681", "Oceania", "Mata-Utu", "XPF",
						"fr"},
				new String[]{"244", "WS", "Samoa", "Samoa", "685", "Oceania", "Apia", "WST", "sm,en"},
				new String[]{"245", "XK", "Kosovo", "Republika e Kosovës", "377,381,383,386", "Europe", "Pristina",
						"EUR", "sq,sr"},
				new String[]{"246", "YE", "Yemen", "اليَمَن", "967", "Asia", "Sana'a", "YER", "ar"},
				new String[]{"247", "YT", "Mayotte", "Mayotte", "262", "Africa", "Mamoudzou", "EUR", "fr"},
				new String[]{"248", "ZA", "South Africa", "South Africa", "27", "Africa", "Pretoria", "ZAR",
						"af,en,nr,st,ss,tn,ts,ve,xh,zu"},
				new String[]{"249", "ZM", "Zambia", "Zambia", "260", "Africa", "Lusaka", "ZMW", "en"},
				new String[]{"250", "ZW", "Zimbabwe", "Zimbabwe", "263", "Africa", "Harare",
						"USD,ZAR,BWP,GBP,AUD,CNY,INR,JPY", "en,sn,nd"}};
	}

}