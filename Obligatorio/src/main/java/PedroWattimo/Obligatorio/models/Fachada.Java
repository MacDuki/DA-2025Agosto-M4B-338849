 private static Fachada instancia;
    // Sistemas como atributos privados.
    // Nunca Gets ni Sets de estos atributos.
    

     private Fachada() {
        //Instanciación de sub-sistemas

    }

        public static Fachada getInstancia() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }


     // Delegación de métodos a los sistemas internos