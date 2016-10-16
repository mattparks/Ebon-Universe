package ebon.universe.celestials;

import flounder.maths.*;

@SuppressWarnings("unused")
public enum Element {
	HYDROGEN(1, 8.99E-5, 1.00794), // 0.0899g/l
	HELIUM(2, 1.785E-4, 4.002602), // 0.1785g/l
	LITHIUM(3, 0.535, 6.941),
	BERYLLIUM(4, 1.848, 9.012182),
	BORON(5, 2.46, 10.811),
	CARBON(6, 2.26, 12.0107),
	NITROGEN(7, 0.001251, 14.0067), // 1.251g/l
	OXYGEN(8, 0.001429, 15.9994), // 1.429g/l
	FLUORINE(9, 0.001696, 18.9984032), // 1.696g/l
	NEON(10, 9.0E-4, 20.1797), // 0.9g/l
	SODIUM(11, 0.968, 22.98977),
	MAGNESIUM(12, 1.738, 24.305),
	ALUMINIUM(13, 2.7, 26.981538),
	SILICON(14, 2.33, 28.0855),
	PHOSPHORUS(15, 1.823, 30.973761),
	SULFUR(16, 1.96, 32.065),
	CHLORINE(17, 0.003214, 35.453), // 3.214g/l
	ARGON(18, 0.001784, 39.948), // 1.784g/l
	POTASSIUM(19, 0.856, 39.0983),
	CALCIUM(20, 1.55, 40.078),
	SCANDIUM(21, 2.985, 44.95591),
	TITANIUM(22, 4.507, 47.867),
	VANADIUM(23, 6.11, 50.9415),
	CHROMIUM(24, 7.14, 51.9961),
	MANGANESE(25, 7.47, 54.938049),
	IRON(26, 7.874, 55.845),
	COBALT(27, 8.9, 58.9332),
	NICKEL(28, 8.908, 58.6934),
	COPPER(29, 8.92, 63.546),
	ZINC(30, 7.14, 65.409),
	GALLIUM(31, 5.904, 69.723),
	GERMANIUM(32, 5.323, 72.64),
	ARSENIC(33, 5.727, 74.9216),
	SELENIUM(34, 4.819, 78.96),
	BROMINE(35, 3.12, 79.904),
	KRYPTON(36, 0.00375, 83.798), // 3.75g/l
	RUBIDIUM(37, 1.532, 85.4678),
	STRONTIUM(38, 2.63, 87.62),
	YTTRIUM(39, 4.472, 88.90585),
	ZIRCONIUM(40, 6.511, 91.224),
	NIOBIUM(41, 8.57, 92.90638),
	MOLYBDENUM(42, 10.28, 95.94),
	TECHNETIUM(43, 11.5, 98.0),
	RUTHENIUM(44, 12.37, 101.07),
	RHODIUM(45, 12.45, 102.9055),
	PALLADIUM(46, 12.023, 106.42),
	SILVER(47, 10.49, 107.8682),
	CADMIUM(48, 8.65, 112.411),
	INDIUM(49, 7.31, 114.818),
	TIN(50, 7.31, 118.71),
	ANTIMONY(51, 6.697, 121.76),
	TELLURIUM(52, 6.24, 127.6),
	IODINE(53, 4.94, 126.90447),
	XENON(54, 0.0059, 131.293), // 5.9g/l
	CESIUM(55, 1.879, 132.90545),
	BARIUM(56, 3.51, 137.327),
	LANTHANUM(57, 6.146, 138.9055),
	CERIUM(58, 6.689, 140.116),
	PRASEODYMIUM(59, 6.64, 140.90765),
	NEODYMIUM(60, 7.01, 144.24),
	PROMETHIUM(61, 7.264, 145.0),
	SAMARIUM(62, 7.353, 150.36),
	EUROPIUM(63, 5.244, 151.964),
	GADOLINIUM(64, 7.901, 157.25),
	TERBIUM(65, 8.219, 158.92534),
	DYSPROSIUM(66, 8.551, 162.5),
	HOLMIUM(67, 8.795, 164.93032),
	ERBIUM(68, 9.066, 167.259),
	THULIUM(69, 9.321, 168.93421),
	YTTERBIUM(70, 6.57, 173.04),
	LUTETIUM(71, 9.841, 174.967),
	HAFNIUM(72, 13.31, 178.49),
	TANTALUM(73, 16.65, 180.9479),
	TUNGSTEN(74, 19.25, 183.84),
	RHENIUM(75, 21.02, 186.207),
	OSMIUM(76, 22.61, 190.23),
	IRIDIUM(77, 22.65, 192.217),
	PLATINUM(78, 21.09, 195.078),
	GOLD(79, 19.3, 196.96655),
	MERCURY(80, 13.534, 200.59),
	THALLIUM(81, 11.85, 204.3833),
	LEAD(82, 11.34, 207.2),
	BISMUTH(83, 9.78, 208.98038),
	POLONIUM(84, 9.196, 209.0),
	ASTATINE(85, -1.0, 210.0),
	RADON(86, 0.00973, 222.0), // 9.73g/l
	FRANCIUM(87, -1.0, 223.0),
	RADIUM(88, 5.0, 226.0),
	ACTINIUM(89, 10.07, 227.0),
	THORIUM(90, 11.724, 232.0381),
	PROTACTINIUM(91, 15.37, 231.03588),
	URANIUM(92, 19.05, 238.02891),
	NEPTUNIUM(93, 20.45, 237.0),
	PLUTONIUM(94, 19.816, 244.0),
	AMERICIUM(95, -1.0, 243.0),
	CURIUM(96, 13.51, 247.0),
	BERKELIUM(97, 14.78, 247.0),
	CALIFORNIUM(98, 15.1, 251.0),
	EINSTEINIUM(99, -1.0, 252.0),
	FERMIUM(100, -1.0, 257.0),
	MENDELEVIUM(101, -1.0, 258.0),
	NOBELIUM(102, -1.0, 259.0),
	LAWRENCIUM(103, -1.0, 262.0),
	RUTHERFORDIUM(104, -1.0, 261.0),
	DUBNIUM(105, -1.0, 262.0),
	SEABORGIUM(106, -1.0, 266.0),
	BOHRIUM(107, -1.0, 264.0),
	HASSIUM(108, -1.0, 277.0),
	MEITNERIUM(109, -1.0, 268.0),
	DARMSTADTIUM(110, -1.0, 281.0),
	ROENTGENIUM(111, -1.0, 272.0),
	UNUNBIUM(112, -1.0, 285.0),
	UNUNTRIUM(113, -1.0, 284.0),
	UNUNQUADIUM(114, -1.0, 289.0),
	UNUNPENTIUM(115, -1.0, 288.0),
	UNUNHEXIUM(116, -1.0, 292.0),
	UNUNSEPTIUM(117, -1.0, -1.0),
	UNUNOCTIUM(118, -1.0, 294.0);

	public static final Element[] VALUES = Element.values();
	public static final int ELEMENTS = 118;

	public final int atomicNumber; // Atomic number, and number of protons, and number of electrons.
	public final double atomicWeight; // The atomic weight of the element.
	public final int neutrons; // Number of neutrons in the element.
	public final double density; // g/cm^3
	public final double weight; // dalton (

	public static void setup() {
		for (int i = 1; i <= ELEMENTS; i++) {
			Element e = getElement(i);

			if (e != null) {
				String data = e.name() + "(" + e.atomicNumber + ", " + e.density + ", " + e.weight + "),";

				if (e.density < 0.01 && e.density != -1.0) {
					data += " // " + Maths.roundToPlace(e.density * 1000, 8) + "g/l";
				}

				System.out.println(data);
			}
		}

		System.exit(0);
	}

	Element(int atomicNumber, double density, double weight) {
		this.atomicNumber = atomicNumber;
		this.atomicWeight = 1;
		this.neutrons = (int) (atomicWeight) - atomicNumber; // Mass number - protons = neutrons.
		this.density = density;
		this.weight = weight;
	}

	public static Element getElement(int atomicNumber) {
		if (atomicNumber <= ELEMENTS && atomicNumber > 0) {
			for (Element e : VALUES) {
				if (atomicNumber == e.atomicNumber) {
					return e;
				}
			}
		}

		return null;
	}
}
