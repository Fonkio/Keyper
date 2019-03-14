package com.example.keyper;

class Generator {

    private String[] spinner;
    private final int MAXTYPES = 3;
    private final int MAXTYPESSPECIALS = 4;
    private int spinnerRdm;

    private final String ALPHAUPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String ALPHALOWER = "abcdefghijklmnopqrstuvwxyz";
    private final int MAXALPHARDM = 26;
    private int alphaRdm;

    private final String DIGITS = "0123456789";
    private final int MAXDIGITRDM = 10;
    private int digitRdm;

    private final String SPECIALS = " !#$%&()*+,-./:;<=>?@[]^_{|}~";
    private final int MAXSPECIALRDM = 29;
    private int specialRdm;

    private boolean specials;

    public Generator(boolean specials) {
        this.specials = specials;

        if(this.specials)
            spinner = new String[MAXTYPESSPECIALS];
        else
            spinner = new String[MAXTYPES];
        spinner[0] = ALPHAUPPER;
        spinner[1] = ALPHALOWER;
        spinner[2] = DIGITS;
        if(this.specials)
            spinner[3] = SPECIALS;
    }

    public String createPassword(int passwdLength) {
        String passwd = "";
        for(int i = 0; i < passwdLength; i++) {
            if(this.specials)
                spinnerRdm = (int)(Math.random()*MAXTYPESSPECIALS);
            else
                spinnerRdm = (int)(Math.random()*MAXTYPES);
            switch(spinnerRdm) {
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
        return passwd;
    }

    public int randomInt(int threshold) {
        return (int)(Math.random()*threshold);
    }
}
