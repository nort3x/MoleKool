package io.github.nort3x.molekool.bind.lammps

enum class InputFileTokens(val token: String) {
    BEGIN(""),
    MASSES("Masses"),
    BOND_COEFFS("Bond Coeffs"),
    ANGLE_COEFFS("Angle Coeffs"),
    DIHEDRAL_COEFFS("Dihedral Coeffs"),
    ATOMS("Atoms"),
    BONDS("Bonds"),
    ANGLES("Angles"),
    DIHEDRALS("Dihedrals"),
    END(""),
}
