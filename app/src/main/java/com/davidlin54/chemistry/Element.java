package com.davidlin54.chemistry;

/**
 * Created by David on 2016-11-13.
 */

public enum Element {

    H(1, "Hydrogen", 1.00794),
    He(2, "Helium", 4.0026),
    Li(3, "Lithium", 6.941),
    Be(4, "Beryllium", 9.01218),
    B(5, "Boron", 10.811),
    C(6, "Carbon", 12.011),
    N(7, "Nitrogen", 14.0067),
    O(8, "Oxygen", 15.9994),
    F(9, "Fluorine", 18.9984),
    Ne(10, "Neon", 20.1797),
    Na(11, "Sodium", 22.98977),
    Mg(12, "Magnesium", 24.305),
    Al(13, "Aluminum", 26.98154),
    Si(14, "Silicon", 28.0855),
    P(15, "Phosphorus", 30.97376),
    S(16, "Sulfur", 32.066),
    Cl(17, "Chlorine", 35.4527),
    Ar(18, "Argon", 39.948),
    K(19, "Potassium", 39.0983),
    Ca(20, "Calcium", 40.078),
    Sc(21, "Scandium", 44.9559),
    Ti(22, "Titanium", 47.88),
    V(23, "Vanadium", 50.9415),
    Cr(24, "Chromium", 51.996),
    Mn(25, "Manganese", 54.938),
    Fe(26, "Iron", 55.847),
    Co(27, "Cobalt", 58.9332),
    Ni(28, "Nickel", 58.6934),
    Cu(29, "Copper", 63.546),
    Zn(30, "Zinc", 65.39),
    Ga(31, "Gallium", 69.723),
    Ge(32, "Germanium", 72.61),
    As(33, "Arsenic", 74.9216),
    Se(34, "Selenium", 78.96),
    Br(35, "Bromine", 79.904),
    Kr(36, "Krypton", 83.8),
    Rb(37, "Rubidium", 85.4678),
    Sr(38, "Strontium", 87.62),
    Y(39, "Yttrium", 88.9059),
    Zr(40, "Zirconium", 91.224),
    Nb(41, "Niobium", 92.9064),
    Mo(42, "Molybdenum", 95.94),
    Tc(43, "Technetium", 98.0),
    Ru(44, "Ruthenium", 101.07),
    Rh(45, "Rhodium", 102.9055),
    Pd(46, "Palladium", 106.42),
    Ag(47, "Silver", 107.868),
    Cd(48, "Cadmium", 112.41),
    In(49, "Indium", 114.82),
    Sn(50, "Tin", 118.71),
    Sb(51, "Antimony", 121.757),
    Te(52, "Tellurium", 127.6),
    I(53, "Iodine", 126.9045),
    Xe(54, "Xenon", 131.29),
    Cs(55, "Cesium", 132.9054),
    Ba(56, "Barium", 137.33),
    La(57, "Lanthanum", 138.9055),
    Ce(58, "Cerium", 140.12),
    Pr(59, "Praseodymium", 140.9077),
    Nd(60, "Neodymium", 144.24),
    Pm(61, "Promethium", 145.0),
    Sm(62, "Samarium", 150.36),
    Eu(63, "Europium", 151.965),
    Gd(64, "Gadolinium", 157.25),
    Tb(65, "Terbium", 158.9253),
    Dy(66, "Dysprosium", 162.5),
    Ho(67, "Holmium", 164.9303),
    Er(68, "Erbium", 167.26),
    Tm(69, "Thulium", 168.9342),
    Yb(70, "Ytterbium", 173.04),
    Lu(71, "Lutetium", 174.967),
    Hf(72, "Hafnium", 178.49),
    Ta(73, "Tantalum", 180.9479),
    W(74, "Tungsten", 183.85),
    Re(75, "Rhenium", 186.207),
    Os(76, "Osmium", 190.2),
    Ir(77, "Iridium", 192.22),
    Pt(78, "Platinum", 195.08),
    Au(79, "Gold", 196.9665),
    Hg(80, "Mercury", 200.59),
    Tl(81, "Thallium", 204.383),
    Pb(82, "Lead", 207.2),
    Bi(83, "Bismuth", 208.9804),
    Po(84, "Polonium", 209.0),
    At(85, "Astatine", 210.0),
    Rn(86, "Radon", 222.0),
    Fr(87, "Francium", 223.0),
    Ra(88, "Radium", 226.0254),
    Ac(89, "Actinium", 227.0),
    Th(90, "Thorium", 232.0381),
    Pa(91, "Protactinium", 231.0359),
    U(92, "Uranium", 238.029),
    Np(93, "Neptunium", 237.0482),
    Pu(94, "Plutonium", 244.0),
    Am(95, "Americium", 243.0),
    Cm(96, "Curium", 247.0),
    Bk(97, "Berkelium", 247.0),
    Cf(98, "Californium", 251.0),
    Es(99, "Einsteinium", 252.0),
    Fm(100, "Fermium", 257.0),
    Md(101, "Mendelevium", 258.0),
    No(102, "Nobelium", 259.0),
    Lr(103, "Lawrencium", 262.0),
    Rf(104, "Rutherfordium", 261.0),
    Db(105, "Dubnium", 262.0),
    Sg(106, "Seaborgium", 263.0),
    Bh(107, "Bohrium", 262.0),
    Hs(108, "Hassium", 265.0),
    Mt(109, "Meitnerium", 266.0),
    Uun(110, "ununnilium", 269.0),
    Uuu(111, "unununium", 272.0),
    Uub(112, "ununbium", 277.0);

    private int mAtomicNumber;
    private String mElementName;
    private double mAtomicWeight;

    Element(int atomicNumber, String elementName, double atomicWeight) {
        this.mAtomicNumber = atomicNumber;
        this.mElementName = elementName;
        this.mAtomicWeight = atomicWeight;
    }

    public String getElementName() {
        return mElementName;
    }

    public int getAtomicNumber() {
        return mAtomicNumber;
    }

    public double getAtomicWeight() {
        return mAtomicWeight;
    }
}
