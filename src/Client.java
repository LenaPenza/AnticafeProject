public class Client {
    /**
     *последний присвоенный индефикатор клиента
     */
    private static int lastId = 0;
    /**
     * уникальный индефикатор клиента
     */
    private int id;

    /**
     * возвращает уникальный индефикатор клиента
     * @return id (уникальный индефикатор клиента)
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор клиента
     * @param id (yовый уникальный идентификатор клиента)
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Присвает уникальный индефикационный номер клиенту. Создается по умолчанию
     */
    public Client() {
        this.id = lastId++;
    }
}
