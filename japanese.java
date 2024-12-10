class Japanese extends Language {
    public Japanese() {
        super("Japoński");  // Wywołanie konstruktora klasy nadrzędnej
    }

    @Override
    public void learn() {
        System.out.println("Nauka japońskiego: zapoznaj się z podstawami gramatyki.");
    }
}

