package com.example.keyper;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterPassword extends ArrayAdapter<Password> {

    //Booleen permettant de savoir si un item de la liste est longclick
    private static boolean isLongClicked = false;

    public AdapterPassword(Context context, ArrayList<Password> pwd) {
        super(context, 0, pwd);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Recupération des données de l'item à la positions donnée
        Password password = getItem(position);

        //Si le l'item est créé (il n'existe pas déjà), alors on inflate la liste
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        //Pour pouvoir utiliser convertView dans la classe anonyme
        final View finalConvertView = convertView;

        //Recupération des éléments qui composent l'item de la liste
        LinearLayout passwordItem = finalConvertView.findViewById(R.id.passwd_item_layout);
        TextView passwordTitle = finalConvertView.findViewById(R.id.title);
        TextView passwordContent = finalConvertView.findViewById(R.id.content);

        //Méthode si longclick sur un item
        passwordItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                //On passe le booleen "isLongClicked" a true pour empecher le déroulement de la méthode de click simple
                AdapterPassword.setIsLongClicked(true);

                LinearLayout passwordItem = (LinearLayout)view.getParent(); //Recuperation de l'élément de la liste cliqué

                ListView passwordList = (ListView)passwordItem.getParent(); //Recuperation de la list complète

                //On met tous les background des items en blanc et on leur associe un tag a false (pour savoir s'ils sont longClicked)
                for (int i = 0; i < passwordList.getChildCount(); i++) {
                    passwordList.getChildAt(i).setBackgroundColor(finalConvertView.getResources().getColor(R.color.white));
                    passwordList.getChildAt(i).setTag(new Boolean(false));
                }



                //On met le background de l'item selectionné en bleu
                passwordItem.setBackgroundColor(finalConvertView.getResources().getColor(R.color.selected));

                //On met sont tag en true (il est longClicked)
                passwordItem.setTag(new Boolean(true));

                //On affiche les icons de modification ou suppression de l'item
                MainActivity.showMenuItem();

                return false;
            }
        });

        //Méthode si simple click sur un item
        passwordItem.setOnClickListener(new View.OnClickListener() {

            private Users db = new Users(getContext()); //Recupération de la BDD

            @Override
            public void onClick(View view) {

                //Si il y a eu un longClick
                if (AdapterPassword.isLongClicked()) {
                    AdapterPassword.setIsLongClicked(false); //On met le booleen isLongClicked a false
                }//Si il n'y a pas eu de long clic
                else {
                    LinearLayout passwordItem = (LinearLayout) view.getParent(); //Recup de l'item de la liste cliqué

                    ListView passwordList = (ListView) passwordItem.getParent(); //Recup de la liste complète

                    //On met tous les background des items en blanc et on leur associe un tag a false (pour savoir s'ils sont longClicked)
                    for (int i = 0; i < passwordList.getChildCount(); i++) {
                        passwordList.getChildAt(i).setBackgroundColor(finalConvertView.getResources().getColor(R.color.white));
                        passwordList.getChildAt(i).setTag(new Boolean(false));
                    }

                    //On cache le menu de modification/supression de l'item, il n'apprait que lors d'un longClick
                    MainActivity.hideMenuItem();

                    TextView passwordContent = (TextView)((LinearLayout)view).getChildAt(1); //Recuperation du champ de mot de passe
                    int passwordContentId = (int)passwordContent.getTag(); //Recupération de l'id du mot de passe stocké dans le tag du champ

                    //Ajout dans le presse-papiers
                    String password = this.db.getPasswordFromId(passwordContentId);
                    AdapterPassword.copyToClipboard(getContext(), password);
                }
            }
        });

        //On met des les champs de l'item les donnees du mot de passe
        passwordTitle.setText(password.getTitle());
        passwordContent.setText(password.getContent());
        passwordContent.setTag(password.getId());

        //On retourne l'item a afficher dans la liste
        return convertView;
    }

    public static void setIsLongClicked(boolean isLongClicked) {
        AdapterPassword.isLongClicked = isLongClicked;
    }

    public static boolean isLongClicked() {
        return isLongClicked;
    }

    //Méthode pour copier dans le presse papier
    public static void copyToClipboard(Context context, String password) {
        String label = "Password";
        Toast.makeText(context, R.string.clipboard_copy, Toast.LENGTH_SHORT).show();
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, password);
        clipboard.setPrimaryClip(clip);
    }
}
