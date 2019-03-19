package com.example.keyper;

class Generator {

    private String[] spinner; //Tableau des Strings utilisées
    private final int MAXTYPES = 3; //Longueur du tableau SANS la String des caracters spéciaux
    private final int MAXTYPESSPECIALS = 4; //Longueur du tableau AVEC la String des caracters spéciaux
    private int spinnerRdm; //Random associé au tableau

    private final String ALPHAUPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //String caractères alphabétique majuscule
    private final String ALPHALOWER = "abcdefghijklmnopqrstuvwxyz"; //String caractères alphabétique minuscule
    private final int MAXALPHARDM = 26; //Longueur des Strings caractères alphabétiques
    private int alphaRdm; //Random associé a la longueur des Strings caractères alphabétiques

    private final String DIGITS = "0123456789"; //String des nombres
    private final int MAXDIGITRDM = 10; //Longueur de la String nombres
    private int digitRdm; //Random associé a la longueur de la String nombre

    private final String SPECIALS = " !#$%&()*+,-./:;<=>?@[]^_{|}~"; //String des caractères spéciaux
    private final int MAXSPECIALRDM = 29; //Longueur de la String caractères spéciaux
    private int specialRdm;//Random associé a la longueur de la String caractères spéciaux

    private boolean specials; //Booleen d'utilisation des caractères spéciaux ou non

    public Generator(boolean specials) {
        this.specials = specials;

        if(this.specials) //Si ob utilise des caractères spéciaux
            spinner = new String[MAXTYPESSPECIALS]; //On créé un tableau de longueur MAXTYPESSPECIAL(4)
        else
            spinner = new String[MAXTYPES];//On créé un tableau de longueur MAXTYPES(3)
        //On ajoute les Strings correspondantes
        spinner[0] = ALPHAUPPER;
        spinner[1] = ALPHALOWER;
        spinner[2] = DIGITS;
        if(this.specials)
            spinner[3] = SPECIALS;
    }

    //Génération d'un mot de passe en fonction d'une longueur
    public String createPassword(int passwdLength) {
        String passwd = "";
        //Boucle jusqu'a la taille de MdP souhaitée
        for(int i = 0; i < passwdLength; i++) {
            if(this.specials)//Si spéciaux utilisés, Random de 0 à MAXTYPESSPECIAL(4) non inclus
                spinnerRdm = (int)(Math.random()*MAXTYPESSPECIALS);
            else
                spinnerRdm = (int)(Math.random()*MAXTYPES);//Sinon Random de 0 à MAXTYPES(3) non inclus
            switch(spinnerRdm) { //En fonction du Random (donc indice) obtenu
                //On récupère un caractère à une position aléatoire de la String située à l'indice du tableau
                //Grace à un random de 0 au max de la longueur de la String correspondante (non inclus)
                case 0:
                    alphaRdm = randomInt(MAXALPHARDM);
                    passwd += ALPHAUPPER.charAt(alphaRdm);
                    break;
                case 1:
                    alphaRdm = randomInt(MAXALPHARDM);
                    passwd += ALPHALOWER.charAt(alphaRdm);
                    break;
                case 2:
                    digitRdm = randomInt(MAXDIGITRDM);
                    passwd += DIGITS.charAt(digitRdm);
                    break;
                case 3:
                    if(this.specials) {
                        specialRdm = randomInt(MAXSPECIALRDM);
                        passwd += SPECIALS.charAt(specialRdm);
                        break;
                    }
            }
        }
        return passwd; //On retourne le MdP généré
    }

    //Retourne un Random de 0 au seuil non inclus
    public int randomInt(int threshold) {
        return (int)(Math.random()*threshold);
    }
}
