package ebon.universe.celestials;

import flounder.maths.*;

import java.util.*;

@SuppressWarnings("unused")
public enum Element {
	HYDROGEN("H", 1, 1.0079, 8.99E-5, 28000.0, new Isotope[]{
			new Isotope(1, 0, 1.007825, 99.99),
			new Isotope(2, 1, 2.014102, 0.015),
	}), // 0.0899g/l
	HELIUM("He", 2, 4.0026, 1.785E-4, 2700.0, new Isotope[]{
			new Isotope(3, 1, 3.016029, 1.0E-4),
			new Isotope(4, 2, 4.002603, 100.0),
	}), // 0.1785g/l
	LITHIUM("Li", 3, 6.941, 0.535, 5.7E-5, new Isotope[]{
			new Isotope(6, 3, 6.015123, 7.42),
			new Isotope(7, 4, 7.016005, 92.58),
	}),
	BERYLLIUM("Be", 4, 9.0122, 1.848, 7.0E-7, new Isotope[]{
			new Isotope(9, 5, 9.012183, 100.0),
	}),
	BORON("B", 5, 10.811, 2.46, 2.1E-5, new Isotope[]{
			new Isotope(10, 5, 10.012938, 19.8),
			new Isotope(11, 6, 11.009305, 80.2),
	}),
	CARBON("C", 6, 12.0107, 2.26, 10.0, new Isotope[]{
			new Isotope(12, 6, 12.0, 98.9),
			new Isotope(13, 7, 13.003355, 1.1),
	}),
	NITROGEN("N", 7, 14.0067, 0.001251, 3.1, new Isotope[]{
			new Isotope(14, 7, 14.003074, 99.63),
			new Isotope(15, 8, 15.000109, 0.37),
	}), // 1.251g/l
	OXYGEN("O", 8, 15.9994, 0.001429, 24.0, new Isotope[]{
			new Isotope(16, 8, 15.994915, 99.76),
			new Isotope(17, 9, 16.999131, 0.038),
			new Isotope(18, 10, 17.999159, 0.2),
	}), // 1.429g/l
	FLUORINE("F", 9, 18.9984, 0.001696, 8.5E-4, new Isotope[]{
			new Isotope(19, 10, 18.998403, 100.0),
	}), // 1.696g/l
	NEON("Ne", 10, 20.1797, 9.0E-4, 3.0, new Isotope[]{
			new Isotope(20, 10, 19.992439, 90.6),
			new Isotope(21, 11, 20.993845, 0.26),
			new Isotope(22, 12, 21.991384, 9.2),
	}), // 0.9g/l
	SODIUM("Na", 11, 22.9897, 0.968, 0.057, new Isotope[]{
			new Isotope(23, 12, 22.98977, 100.0),
	}),
	MAGNESIUM("Mg", 12, 24.305, 1.738, 1.1, new Isotope[]{
			new Isotope(24, 12, 23.985045, 78.9),
			new Isotope(25, 13, 24.985839, 10.0),
			new Isotope(26, 14, 25.982595, 11.1),
	}),
	ALUMINIUM("Al", 13, 26.9815, 2.7, 0.085, new Isotope[]{
			new Isotope(27, 14, 26.981541, 100.0),
	}),
	SILICON("Si", 14, 28.0855, 2.33, 1.0, new Isotope[]{
			new Isotope(28, 14, 27.976928, 92.23),
			new Isotope(29, 15, 28.976496, 4.67),
			new Isotope(30, 16, 29.973772, 3.1),
	}),
	PHOSPHORUS("P", 15, 30.9738, 1.823, 0.01, new Isotope[]{
			new Isotope(31, 16, 30.973763, 100.0),
	}),
	SULFUR("S", 16, 32.065, 1.96, 0.52, new Isotope[]{
			new Isotope(32, 16, 31.972072, 95.02),
			new Isotope(33, 17, 32.971459, 0.75),
			new Isotope(34, 18, 33.967868, 4.21),
			new Isotope(36, 20, 35.967079, 0.02),
	}),
	CHLORINE("Cl", 17, 35.453, 0.003214, 0.0052, new Isotope[]{
			new Isotope(35, 18, 34.968853, 75.77),
			new Isotope(37, 20, 36.965903, 24.23),
	}), // 3.214g/l
	ARGON("Ar", 18, 39.948, 0.001784, 0.1, new Isotope[]{
			new Isotope(36, 18, 35.967546, 0.34),
			new Isotope(38, 20, 37.962732, 0.063),
			new Isotope(40, 22, 39.962383, 99.6),
	}), // 1.784g/l
	POTASSIUM("K", 19, 39.0983, 0.856, 0.0038, new Isotope[]{
			new Isotope(39, 20, 38.963708, 93.2),
			new Isotope(40, 21, 39.963999, 0.012),
			new Isotope(41, 22, 40.961825, 6.73),
	}),
	CALCIUM("Ca", 20, 40.078, 1.55, 0.061, new Isotope[]{
			new Isotope(40, 20, 39.962591, 96.95),
			new Isotope(42, 22, 41.958622, 0.65),
			new Isotope(43, 23, 42.95877, 0.14),
			new Isotope(44, 24, 43.955485, 2.086),
			new Isotope(46, 26, 45.953689, 0.004),
			new Isotope(48, 28, 47.952532, 0.19),
	}),
	SCANDIUM("Sc", 21, 44.9559, 2.985, 3.4E-5, new Isotope[]{
			new Isotope(45, 24, 44.955914, 100.0),
	}),
	TITANIUM("Ti", 22, 47.867, 4.507, 0.0024, new Isotope[]{
			new Isotope(46, 24, 45.952633, 8.0),
			new Isotope(47, 25, 46.951765, 7.3),
			new Isotope(48, 26, 47.947947, 73.8),
			new Isotope(49, 27, 48.947871, 5.5),
			new Isotope(50, 28, 49.944786, 5.4),
	}),
	VANADIUM("V", 23, 50.9415, 6.11, 2.9E-4, new Isotope[]{
			new Isotope(50, 27, 49.947161, 0.25),
			new Isotope(51, 28, 50.943963, 99.75),
	}),
	CHROMIUM("Cr", 24, 51.9961, 7.14, 0.013, new Isotope[]{
			new Isotope(50, 26, 49.946046, 4.35),
			new Isotope(52, 28, 51.94051, 83.79),
			new Isotope(53, 29, 52.940651, 9.5),
			new Isotope(54, 30, 53.938882, 2.36),
	}),
	MANGANESE("Mn", 25, 54.938, 7.47, 0.0095, new Isotope[]{
			new Isotope(55, 30, 54.938046, 100.0),
	}),
	IRON("Fe", 26, 55.845, 7.874, 0.9, new Isotope[]{
			new Isotope(54, 28, 53.939612, 5.8),
			new Isotope(56, 30, 55.934939, 91.72),
			new Isotope(57, 31, 56.935396, 2.2),
			new Isotope(58, 32, 57.933278, 0.28),
	}),
	COBALT("Co", 27, 58.9332, 8.9, 0.0023, new Isotope[]{
			new Isotope(59, 32, 58.933198, 100.0),
	}),
	NICKEL("Ni", 28, 58.6934, 8.908, 0.05, new Isotope[]{
			new Isotope(58, 30, 57.935347, 68.27),
			new Isotope(60, 32, 59.930789, 26.1),
			new Isotope(61, 33, 60.931059, 1.13),
			new Isotope(62, 34, 61.928346, 3.59),
			new Isotope(64, 36, 63.927968, 0.91),
	}),
	COPPER("Cu", 29, 63.546, 8.92, 5.2E-4, new Isotope[]{
			new Isotope(63, 34, 62.929599, 69.17),
			new Isotope(65, 36, 64.927792, 30.83),
	}),
	ZINC("Zn", 30, 65.39, 7.14, 0.0013, new Isotope[]{
			new Isotope(64, 34, 63.929145, 48.6),
			new Isotope(66, 36, 65.926035, 27.9),
			new Isotope(67, 37, 66.927129, 4.1),
			new Isotope(68, 38, 67.924846, 18.8),
			new Isotope(70, 40, 69.925325, 0.6),
	}),
	GALLIUM("Ga", 31, 69.723, 5.904, 3.8E-5, new Isotope[]{
			new Isotope(69, 38, 68.925581, 60.1),
			new Isotope(71, 40, 70.924701, 39.9),
	}),
	GERMANIUM("Ge", 32, 72.64, 5.323, 1.2E-4, new Isotope[]{
			new Isotope(70, 38, 69.92425, 20.5),
			new Isotope(72, 40, 71.92208, 27.4),
			new Isotope(73, 41, 72.923464, 7.8),
			new Isotope(74, 42, 73.921179, 36.5),
			new Isotope(76, 44, 75.921403, 7.8),
	}),
	ARSENIC("As", 33, 74.9216, 5.727, 6.6E-6, new Isotope[]{
			new Isotope(75, 42, 74.921596, 100.0),
	}),
	SELENIUM("Se", 34, 78.96, 4.819, 6.3E-5, new Isotope[]{
			new Isotope(74, 40, 73.922477, 0.9),
			new Isotope(76, 42, 75.919207, 9.0),
			new Isotope(77, 43, 76.919908, 7.6),
			new Isotope(78, 44, 77.917304, 23.5),
			new Isotope(80, 46, 79.916521, 49.6),
			new Isotope(82, 48, 81.916709, 9.4),
	}),
	BROMINE("Br", 35, 79.904, 3.12, 1.2E-5, new Isotope[]{
			new Isotope(79, 44, 78.918336, 50.69),
			new Isotope(81, 46, 80.91629, 49.31),
	}),
	KRYPTON("Kr", 36, 83.8, 0.00375, 4.8E-5, new Isotope[]{
			new Isotope(78, 42, 77.920397, 0.35),
			new Isotope(80, 44, 79.916375, 2.25),
			new Isotope(82, 46, 81.913483, 11.6),
			new Isotope(83, 47, 82.914134, 11.5),
			new Isotope(84, 48, 83.911506, 57.0),
			new Isotope(86, 50, 85.910614, 17.3),
	}), // 3.75g/l
	RUBIDIUM("Rb", 37, 85.4678, 1.532, 7.0E-6, new Isotope[]{
			new Isotope(85, 48, 84.9118, 72.17),
			new Isotope(87, 50, 86.909184, 27.84),
	}),
	STRONTIUM("Sr", 38, 87.62, 2.63, 2.4E-5, new Isotope[]{
			new Isotope(84, 46, 83.913428, 0.56),
			new Isotope(86, 48, 85.909273, 9.86),
			new Isotope(87, 49, 86.908902, 7.0),
			new Isotope(88, 50, 87.905625, 82.58),
	}),
	YTTRIUM("Y", 39, 88.9059, 4.472, 4.6E-6, new Isotope[]{
			new Isotope(89, 50, 88.905856, 100.0),
	}),
	ZIRCONIUM("Zr", 40, 91.224, 6.511, 1.14E-5, new Isotope[]{
			new Isotope(90, 50, 89.904708, 51.45),
			new Isotope(91, 51, 90.905644, 11.27),
			new Isotope(92, 52, 91.905039, 17.17),
			new Isotope(94, 54, 93.906319, 17.33),
			new Isotope(96, 56, 95.908272, 2.78),
	}),
	NIOBIUM("Nb", 41, 92.9064, 8.57, 7.0E-7, new Isotope[]{
			new Isotope(93, 52, 92.906378, 100.0),
	}),
	MOLYBDENUM("Mo", 42, 95.94, 10.28, 2.6E-6, new Isotope[]{
			new Isotope(92, 50, 91.906809, 14.84),
			new Isotope(94, 52, 93.905086, 9.25),
			new Isotope(95, 53, 94.905838, 15.92),
			new Isotope(96, 54, 95.904676, 16.68),
			new Isotope(97, 55, 96.906018, 9.55),
			new Isotope(98, 56, 97.905405, 24.13),
			new Isotope(100, 58, 99.907473, 9.63),
	}),
	TECHNETIUM("Tc", 43, 98.0, 11.5, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	RUTHENIUM("Ru", 44, 101.07, 12.37, 1.9E-6, new Isotope[]{
			new Isotope(96, 52, 95.907596, 5.52),
			new Isotope(98, 54, 97.905287, 1.88),
			new Isotope(99, 55, 98.905937, 12.7),
			new Isotope(100, 56, 99.904218, 12.6),
			new Isotope(101, 57, 100.905581, 17.0),
			new Isotope(102, 58, 101.904348, 31.6),
			new Isotope(104, 60, 103.905422, 18.7),
	}),
	RHODIUM("Rh", 45, 102.9055, 12.45, 3.4E-7, new Isotope[]{
			new Isotope(103, 58, 102.905503, 100.0),
	}),
	PALLADIUM("Pd", 46, 106.42, 12.023, 1.4E-6, new Isotope[]{
			new Isotope(102, 56, 101.905609, 1.02),
			new Isotope(104, 58, 103.904026, 11.14),
			new Isotope(105, 59, 104.905075, 22.33),
			new Isotope(106, 60, 105.903475, 27.33),
			new Isotope(108, 62, 107.903894, 26.46),
			new Isotope(110, 64, 109.905169, 11.72),
	}),
	SILVER("Ag", 47, 107.8682, 10.49, 4.9E-7, new Isotope[]{
			new Isotope(107, 60, 106.905095, 51.84),
			new Isotope(109, 62, 108.904754, 48.16),
	}),
	CADMIUM("Cd", 48, 112.411, 8.65, 1.6E-6, new Isotope[]{
			new Isotope(106, 58, 105.906461, 1.25),
			new Isotope(108, 60, 107.904186, 0.89),
			new Isotope(110, 62, 109.903007, 12.49),
			new Isotope(111, 63, 110.904182, 12.8),
			new Isotope(112, 64, 111.902761, 24.13),
			new Isotope(113, 65, 112.904401, 12.22),
			new Isotope(114, 66, 113.903361, 28.73),
			new Isotope(116, 68, 115.904758, 7.49),
	}),
	INDIUM("In", 49, 114.818, 7.31, 1.9E-7, new Isotope[]{
			new Isotope(113, 64, 112.904056, 4.3),
			new Isotope(115, 66, 114.903875, 95.7),
	}),
	TIN("Sn", 50, 118.71, 7.31, 3.9E-6, new Isotope[]{
			new Isotope(112, 62, 111.904826, 0.97),
			new Isotope(114, 64, 113.902784, 0.65),
			new Isotope(115, 65, 114.903348, 0.36),
			new Isotope(116, 66, 115.901744, 14.7),
			new Isotope(117, 67, 116.902954, 7.7),
			new Isotope(118, 68, 117.901607, 24.3),
			new Isotope(119, 69, 118.90331, 8.6),
			new Isotope(120, 70, 119.902199, 32.4),
			new Isotope(122, 72, 121.90344, 4.6),
			new Isotope(124, 74, 123.905271, 5.6),
	}),
	ANTIMONY("Sb", 51, 121.76, 6.697, 3.1E-7, new Isotope[]{
			new Isotope(121, 70, 120.903824, 57.3),
			new Isotope(123, 72, 122.904222, 42.7),
	}),
	TELLURIUM("Te", 52, 127.6, 6.24, 4.9E-6, new Isotope[]{
			new Isotope(120, 68, 119.904021, 0.096),
			new Isotope(122, 70, 121.903055, 2.6),
			new Isotope(123, 71, 122.904278, 0.91),
			new Isotope(124, 72, 123.902825, 4.82),
			new Isotope(125, 73, 124.904435, 7.14),
			new Isotope(126, 74, 125.90331, 18.95),
			new Isotope(128, 76, 127.904464, 31.69),
			new Isotope(130, 78, 129.906229, 33.8),
	}),
	IODINE("I", 53, 126.9045, 4.94, 9.0E-7, new Isotope[]{
			new Isotope(127, 74, 126.904477, 100.0),
	}),
	XENON("Xe", 54, 131.293, 0.0059, 4.8E-6, new Isotope[]{
			new Isotope(124, 70, 123.905894, 0.1),
			new Isotope(126, 72, 125.904281, 0.09),
			new Isotope(128, 74, 127.903531, 1.91),
			new Isotope(129, 75, 128.90478, 26.4),
			new Isotope(130, 76, 129.90351, 4.1),
			new Isotope(131, 77, 130.905076, 21.2),
			new Isotope(132, 78, 131.904148, 26.9),
			new Isotope(134, 80, 133.905395, 10.4),
			new Isotope(136, 82, 135.907219, 8.9),
	}), // 5.9g/l
	CESIUM("Cs", 55, 132.9055, 1.879, -1.0, new Isotope[]{
			new Isotope(133, 78, 132.905433, 100.0),
	}),
	BARIUM("Ba", 56, 137.327, 3.51, 4.5E-6, new Isotope[]{
			new Isotope(130, 74, 129.906277, 0.11),
			new Isotope(132, 76, 131.905042, 0.1),
			new Isotope(134, 78, 133.90449, 2.42),
			new Isotope(135, 79, 134.905668, 6.59),
			new Isotope(136, 80, 135.904556, 7.85),
			new Isotope(137, 81, 136.905816, 11.23),
			new Isotope(138, 82, 137.905236, 71.7),
	}),
	LANTHANUM("La", 57, 138.9055, 6.146, 4.4E-7, new Isotope[]{
			new Isotope(138, 81, 137.907114, 0.09),
			new Isotope(139, 82, 138.906355, 99.91),
	}),
	CERIUM("Ce", 58, 140.116, 6.689, 1.1E-6, new Isotope[]{
			new Isotope(136, 78, 135.90714, 0.19),
			new Isotope(138, 80, 137.905996, 0.25),
			new Isotope(140, 82, 139.905442, 88.48),
			new Isotope(142, 84, 141.909249, 11.08),
	}),
	PRASEODYMIUM("Pr", 59, 140.9077, 6.64, 1.7E-7, new Isotope[]{
			new Isotope(141, 82, 140.907657, 100.0),
	}),
	NEODYMIUM("Nd", 60, 144.24, 7.01, 8.3E-7, new Isotope[]{
			new Isotope(142, 82, 141.907731, 27.13),
			new Isotope(143, 83, 142.909823, 12.18),
			new Isotope(144, 84, 143.910096, 23.8),
			new Isotope(145, 85, 144.912582, 8.3),
			new Isotope(146, 86, 145.913126, 17.19),
			new Isotope(148, 88, 147.916901, 5.76),
			new Isotope(150, 90, 149.9209, 5.64),
	}),
	PROMETHIUM("Pm", 61, 145.0, 7.264, -1.0, new Isotope[]{
			new Isotope(145, 84, 145.0, 100.0),
	}),
	SAMARIUM("Sm", 62, 150.36, 7.353, 2.6E-7, new Isotope[]{
			new Isotope(144, 82, 143.912009, 3.1),
			new Isotope(147, 85, 146.914907, 15.0),
			new Isotope(148, 86, 147.914832, 11.3),
			new Isotope(149, 87, 148.917193, 13.8),
			new Isotope(150, 88, 149.917285, 7.4),
			new Isotope(152, 90, 151.919741, 26.7),
			new Isotope(154, 92, 153.922218, 22.7),
	}),
	EUROPIUM("Eu", 63, 151.964, 5.244, 9.7E-8, new Isotope[]{
			new Isotope(151, 88, 150.91986, 47.8),
			new Isotope(153, 90, 152.921243, 52.2),
	}),
	GADOLINIUM("Gd", 64, 157.25, 7.901, 3.3E-7, new Isotope[]{
			new Isotope(152, 88, 151.919803, 0.2),
			new Isotope(154, 90, 153.920876, 2.18),
			new Isotope(155, 91, 154.822629, 14.8),
			new Isotope(156, 92, 155.92213, 20.47),
			new Isotope(157, 93, 156.923967, 15.65),
			new Isotope(158, 94, 157.924111, 24.84),
			new Isotope(160, 96, 159.927061, 21.86),
	}),
	TERBIUM("Tb", 65, 158.9253, 8.219, 6.0E-8, new Isotope[]{
			new Isotope(159, 94, 158.92535, 100.0),
	}),
	DYSPROSIUM("Dy", 66, 162.5, 8.551, 4.0E-7, new Isotope[]{
			new Isotope(156, 90, 155.924287, 0.06),
			new Isotope(158, 92, 157.924412, 0.1),
			new Isotope(160, 94, 159.925203, 2.34),
			new Isotope(161, 95, 160.926939, 18.9),
			new Isotope(162, 96, 161.926805, 25.5),
			new Isotope(163, 97, 162.928737, 24.9),
			new Isotope(164, 98, 163.929183, 28.2),
	}),
	HOLMIUM("Ho", 67, 164.9303, 8.795, 8.9E-8, new Isotope[]{
			new Isotope(165, 98, 164.930332, 100.0),
	}),
	ERBIUM("Er", 68, 167.259, 9.066, 6.0E-8, new Isotope[]{
			new Isotope(162, 94, 161.928787, 0.14),
			new Isotope(164, 96, 163.929211, 1.61),
			new Isotope(166, 98, 165.930305, 33.6),
			new Isotope(167, 99, 166.932061, 22.95),
			new Isotope(168, 100, 167.932383, 26.8),
			new Isotope(170, 102, 169.935476, 14.9),
	}),
	THULIUM("Tm", 69, 168.9342, 9.321, 3.8E-8, new Isotope[]{
			new Isotope(169, 100, 168.934225, 100.0),
	}),
	YTTERBIUM("Yb", 70, 173.04, 6.57, 2.5E-7, new Isotope[]{
			new Isotope(168, 98, 167.933908, 0.13),
			new Isotope(170, 100, 169.934774, 3.05),
			new Isotope(171, 101, 170.936338, 14.3),
			new Isotope(172, 102, 171.936393, 21.9),
			new Isotope(173, 103, 172.938222, 16.12),
			new Isotope(174, 104, 173.938873, 31.8),
			new Isotope(176, 106, 175.942576, 12.7),
	}),
	LUTETIUM("Lu", 71, 174.967, 9.841, 3.7E-8, new Isotope[]{
			new Isotope(175, 104, 174.940785, 97.4),
			new Isotope(176, 105, 175.942694, 2.6),
	}),
	HAFNIUM("Hf", 72, 178.49, 13.31, 1.5E-7, new Isotope[]{
			new Isotope(174, 102, 173.940065, 0.16),
			new Isotope(176, 104, 175.94142, 5.2),
			new Isotope(177, 105, 176.943233, 18.6),
			new Isotope(178, 106, 177.94371, 27.1),
			new Isotope(179, 107, 178.945827, 13.74),
			new Isotope(180, 108, 179.946561, 35.2),
	}),
	TANTALUM("Ta", 73, 180.9479, 16.65, 3.8E-8, new Isotope[]{
			new Isotope(180, 107, 179.947489, 0.012),
			new Isotope(181, 108, 180.948014, 99.99),
	}),
	TUNGSTEN("W", 74, 183.84, 19.25, 1.3E-7, new Isotope[]{
			new Isotope(180, 106, 179.946727, 0.13),
			new Isotope(182, 108, 181.948225, 26.3),
			new Isotope(183, 109, 182.950245, 14.3),
			new Isotope(184, 110, 183.950953, 30.67),
			new Isotope(186, 112, 185.954377, 28.6),
	}),
	RHENIUM("Re", 75, 186.207, 21.02, 5.0E-8, new Isotope[]{
			new Isotope(185, 110, 184.952977, 37.4),
			new Isotope(187, 112, 186.955765, 62.6),
	}),
	OSMIUM("Os", 76, 190.23, 22.61, 6.7E-7, new Isotope[]{
			new Isotope(184, 108, 183.952514, 0.02),
			new Isotope(186, 110, 185.953852, 1.58),
			new Isotope(187, 111, 186.955762, 1.6),
			new Isotope(188, 112, 187.95585, 13.3),
			new Isotope(189, 113, 188.958156, 16.1),
			new Isotope(190, 114, 189.958455, 26.4),
			new Isotope(192, 116, 191.961487, 41.0),
	}),
	IRIDIUM("Ir", 77, 192.217, 22.65, 6.6E-7, new Isotope[]{
			new Isotope(191, 114, 190.960603, 37.3),
			new Isotope(193, 116, 192.962942, 62.7),
	}),
	PLATINUM("Pt", 78, 195.078, 21.09, 1.34E-6, new Isotope[]{
			new Isotope(190, 112, 189.959937, 0.01),
			new Isotope(192, 114, 191.961049, 0.79),
			new Isotope(194, 116, 193.962679, 32.9),
			new Isotope(195, 117, 194.964785, 33.8),
			new Isotope(196, 118, 195.964947, 25.3),
			new Isotope(198, 120, 197.967879, 7.2),
	}),
	GOLD("Au", 79, 196.9665, 19.3, 1.9E-7, new Isotope[]{
			new Isotope(197, 118, 196.96656, 100.0),
	}),
	MERCURY("Hg", 80, 200.59, 13.534, 3.4E-7, new Isotope[]{
			new Isotope(196, 116, 195.965812, 0.15),
			new Isotope(198, 118, 197.96676, 10.1),
			new Isotope(199, 119, 198.968269, 17.0),
			new Isotope(200, 120, 199.968316, 23.1),
			new Isotope(201, 121, 200.970293, 13.2),
			new Isotope(202, 122, 201.970632, 29.65),
			new Isotope(204, 124, 203.973481, 6.8),
	}),
	THALLIUM("Tl", 81, 204.3833, 11.85, 1.9E-7, new Isotope[]{
			new Isotope(203, 122, 202.972336, 29.52),
			new Isotope(205, 124, 204.97441, 70.48),
	}),
	LEAD("Pb", 82, 207.2, 11.34, 3.1E-6, new Isotope[]{
			new Isotope(204, 122, 203.973037, 1.4),
			new Isotope(206, 124, 205.974455, 24.1),
			new Isotope(207, 125, 206.975885, 22.1),
			new Isotope(208, 126, 207.976641, 52.4),
	}),
	BISMUTH("Bi", 83, 208.9804, 9.78, 1.4E-7, new Isotope[]{
			new Isotope(209, 126, 208.980388, 100.0),
	}),
	POLONIUM("Po", 84, 209.0, 9.196, -1.0, new Isotope[]{
			new Isotope(209, 125, 209.0, 100.0),
	}),
	ASTATINE("At", 85, 210.0, 7.0, -1.0, new Isotope[]{
			new Isotope(210, 125, 210.0, 100.0),
	}),
	RADON("Rn", 86, 222.0, 0.00973, -1.0, new Isotope[]{
			new Isotope(222, 136, 222.0, 100.0),
	}), // 9.73g/l
	FRANCIUM("Fr", 87, 223.0, 1.873, -1.0, new Isotope[]{
			new Isotope(223, 136, 223.0, 100.0),
	}),
	RADIUM("Ra", 88, 226.0, 5.0, -1.0, new Isotope[]{
			new Isotope(226, 138, 226.0, 100.0),
	}),
	ACTINIUM("Ac", 89, 227.0, 10.07, -1.0, new Isotope[]{
			new Isotope(227, 138, 227.0, 100.0),
	}),
	THORIUM("Th", 90, 232.038054, 11.724, 4.5E-8, new Isotope[]{
			new Isotope(232, 142, 232.038054, 100.0),
	}),
	PROTACTINIUM("Pa", 91, 231.0359, 15.37, -1.0, new Isotope[]{
			new Isotope(231, 140, 231.0359, 100.0),
	}),
	URANIUM("U", 92, 238.0289, 19.05, 1.8E-8, new Isotope[]{
			new Isotope(234, 142, 234.040947, 0.006),
			new Isotope(235, 143, 235.043925, 0.72),
			new Isotope(238, 146, 238.050786, 99.27),
	}),
	NEPTUNIUM("Np", 93, 237.0, 20.45, -1.0, new Isotope[]{
			new Isotope(237, 144, 237.0, 100.0),
	}),
	PLUTONIUM("Pu", 94, 244.0, 19.816, -1.0, new Isotope[]{
			new Isotope(244, 150, 244.0, 100.0),
	}),
	AMERICIUM("Am", 95, 243.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	CURIUM("Cm", 96, 247.0, 13.51, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	BERKELIUM("Bk", 97, 247.0, 14.78, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	CALIFORNIUM("Cf", 98, 251.0, 15.1, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	EINSTEINIUM("Es", 99, 252.0, 8.84, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	FERMIUM("Fm", 100, 257.0, 8.84, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	MENDELEVIUM("Md", 101, 258.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	NOBELIUM("No", 102, 259.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	LAWRENCIUM("Lr", 103, 262.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	RUTHERFORDIUM("Rf", 104, 261.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	DUBNIUM("Db", 105, 262.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	SEABORGIUM("Sg", 106, 266.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	BOHRIUM("Bh", 107, 264.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	HASSIUM("Hs", 108, 277.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	MEITNERIUM("Mt", 109, 268.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	DARMSTADTIUM("Ds", 110, -1.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	ROENTGENIUM("Rg", 111, 272.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	COPERNICIUM("Cn", 112, -1.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	UNUNTRIUM("Uut", 113, -1.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	FLEROVIUM("Fl", 114, -1.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	UNUNPENTIUM("Uup", 115, -1.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	LIVERMORIUM("Lv", 116, -1.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	UNUNSEPTIUM("Uus", 117, -1.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	}),
	UNUNOCTIUM("Uuo", 118, -1.0, -1.0, -1.0, new Isotope[]{
			// No Know Isotopes.
	});

	public static final Element[] VALUES = Element.values();
	public static final int ELEMENTS = 118;

	public final String symbol;
	public final int atomicNumber; // Atomic number, and number of protons, and number of electrons.
	public final double atomicWeight; // The weighted average atomic weight of the element.
	public final double density; // The weighted average atomic density (g/cm^3).
	public final double universeMakeup; // Atom mole fraction relative to silicon = 1.
	public final Isotope[] isotopes; // A list of isotopes for the element.

	Element(String symbol, int atomicNumber, double atomicWeight, double density, double universeMakeup, Isotope[] isotopes) {
		this.symbol = symbol;
		this.atomicNumber = atomicNumber;
		this.atomicWeight = atomicWeight;
		this.density = density;

		this.universeMakeup = universeMakeup;

		this.isotopes = isotopes;
	}

	public static void createTemp() {
		String s = "1=H=Hydrogen,2=He=Helium,3=Li=Lithium,4=Be=Beryllium,5=B=Boron,6=C=Carbon,7=N=Nitrogen,8=O=Oxygen,9=F=Fluorine,10=Ne=Neon,11=Na=Sodium,12=Mg=Magnesium,13=Al=Aluminium,14=Si=Silicon,15=P=Phosphorus,16=S=Sulfur,17=Cl=Chlorine,18=Ar=Argon,19=K=Potassium,20=Ca=Calcium,21=Sc=Scandium,22=Ti=Titanium,23=V=Vanadium,24=Cr=Chromium,25=Mn=Manganese,26=Fe=Iron,27=Co=Cobalt,28=Ni=Nickel,29=Cu=Copper,30=Zn=Zinc,31=Ga=Gallium,32=Ge=Germanium,33=As=Arsenic,34=Se=Selenium,35=Br=Bromine,36=Kr=Krypton,37=Rb=Rubidium,38=Sr=Strontium,39=Y=Yttrium,40=Zr=Zirconium,41=Nb=Niobium,42=Mo=Molybdenum,43=Tc=Technetium,44=Ru=Ruthenium,45=Rh=Rhodium,46=Pd=Palladium,47=Ag=Silver,48=Cd=Cadmium,49=In=Indium,50=Sn=Tin,51=Sb=Antimony,52=Te=Tellurium,53=I=Iodine,54=Xe=Xenon,55=Cs=Cesium,56=Ba=Barium,57=La=Lanthanum,58=Ce=Cerium,59=Pr=Praseodymium,60=Nd=Neodymium,61=Pm=Promethium,62=Sm=Samarium,63=Eu=Europium,64=Gd=Gadolinium,65=Tb=Terbium,66=Dy=Dysprosium,67=Ho=Holmium,68=Er=Erbium,69=Tm=Thulium,70=Yb=Ytterbium,71=Lu=Lutetium,72=Hf=Hafnium,73=Ta=Tantalum,74=W=Tungsten,75=Re=Rhenium,76=Os=Osmium,77=Ir=Iridium,78=Pt=Platinum,79=Au=Gold,80=Hg=Mercury,81=Tl=Thallium,82=Pb=Lead,83=Bi=Bismuth,84=Po=Polonium,85=At=Astatine,86=Rn=Radon,87=Fr=Francium,88=Ra=Radium,89=Ac=Actinium,90=Th=Thorium,91=Pa=Protactinium,92=U=Uranium,93=Np=Neptunium,94=Pu=Plutonium,95=Am=Americium,96=Cm=Curium,97=Bk=Berkelium,98=Cf=Californium,99=Es=Einsteinium,100=Fm=Fermium,101=Md=Mendelevium,102=No=Nobelium,103=Lr=Lawrencium,104=Rf=Rutherfordium,105=Db=Dubnium,106=Sg=Seaborgium,107=Bh=Bohrium,108=Hs=Hassium,109=Mt=Meitnerium,110=Ds=Darmstadtium,111=Rg=Roentgenium,112=Cn=Copernicium,113=Uut=Ununtrium,114=Fl=Flerovium,115=Uup=Ununpentium,116=Lv=Livermorium,117=Uus=Ununseptium,118=Uuo=Ununoctium,";
		String[] d = s.split(",");
		Map<String, String> v = new HashMap<>();

		for (String i : d) {
			String value = i.split("=")[1].trim();
			String key = i.split("=")[2].trim();
			v.put(key.toUpperCase(), value);
		}

		for (int i = 1; i <= ELEMENTS; i++) {
			Element e = getElement(i);

			if (e != null) {
				String data = e.name() + "(\"" + e.symbol + "\", " + e.atomicNumber + ", " + e.atomicWeight + ", " + e.density + ", " + e.universeMakeup + ", new Isotope[]{ ";

				if (e.isotopes.length != 0) {
					for (Isotope isotope : e.isotopes) {
						data += "\n     new Isotope(" + isotope.symbol + ", " + isotope.neutrons + ", " + isotope.atomicWeight + ", " + (e.isotopes.length == 1 ? 100.0 : isotope.abundance) + "), ";
					}
				} else {
					data += "\n     // No Know Isotopes.";
				}

				data += " \n})" + (i == ELEMENTS ? ";" : ",");

				if (e.density < 0.01 && e.density != -1.0) {
					data += " // " + Maths.roundToPlace(e.density * 1000, 8) + "g/l";
				}

				System.out.println(data);
			}
		}

		System.exit(0);
	}

	public static Element getElement(int atomicNumber) {
		if (atomicNumber <= ELEMENTS && atomicNumber > 0) {
			for (Element e : VALUES) {
				if (atomicNumber == e.atomicNumber) {
					return e;
				}
			}
		}

		throw new NullPointerException(atomicNumber + " is not a atomic number!");
	}

	public static class Isotope {
		public final int symbol; // The mass number, equal to the number of protons and neutrons in the isotope.
		public final int neutrons; // Number of neutrons in the element.
		public final double atomicWeight; // The atomic weight of the element.
		public final double abundance; // The abundance found in the universe (1-100).

		protected Isotope(int symbol, int neutrons, double atomicWeight, double abundance) {
			this.symbol = symbol;
			this.neutrons = neutrons;
			this.atomicWeight = atomicWeight;
			this.abundance = abundance;
		}
	}
}
