package asot.me.rest.dom;

public enum GenreEnums {
    ACTION(1),
    ADVENTURE(2),
    ANIMATION(3),
    COMEDY(4),
    CRIME(5),
    DOCUMENTARY(6),
    DRAMA(7),
    FAMILY(8),
    FANTASY(9),
    HISTORY(10),
    HORROR(11),
    MUSIC(12),
    MYSTERY(13),
    ROMANCE(14),
    SCIENCE_FICTION(15),
    TV_MOVIE(16),
    THRILLER(17),
    WAR(18),
    WESTERN(19);

    private final int id;

    GenreEnums(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
