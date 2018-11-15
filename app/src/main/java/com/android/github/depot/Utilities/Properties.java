package com.android.github.depot.Utilities;

public class Properties {

    /* Default construtor */

    public Properties() {
    }
    /* Fin */

    /* Host de l'api */
    public String getHostAPI(){
        return "https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc";
    }
    /* Fin */



}
