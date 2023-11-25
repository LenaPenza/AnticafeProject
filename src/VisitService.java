import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitService {
    /**
     * Цена за минуту пребывания клиента в ресторане
     */
    private static double pricePerMinute = 5;
    /**
     * Список всех посещений в ресторане
     */
   static List<Visit> visits = new ArrayList<>();

    /**
     * Создает новое посещение клиентом ресторана и связывает его с указанным столом
     * @param client клиент, совершающий посещение
     * @param tableId идентификатор стола, на который бронируется посещение
     * @return объект Visit, представляющий созданное посещение
     */
    public static Visit createVisit(Client client, int tableId){
        Table table = TableService.tables.get(tableId);
        Visit visit = new Visit(client, table, LocalDateTime.now());
        table.setFree(false);
        visits.add(visit);
        return visit;
    }

    /**
     * Завершает указанное посещение по идентификатору стола, рассчитывает его продолжительность и стоимость
     * Меняет статус завершенности посещения на true и освобождает стол
     * @param tableId bдентификатор стола, для которого завершается посещение
     * @return объект Visit, представляющий завершенное посещение
     */
    public static Visit finishVisit(int tableId){
        Table table = TableService.tables.get(tableId);
        Visit visit = visits.stream()
                .filter(v -> v.getTable().getId() == tableId && !v.isFinished())
                .findFirst().orElseThrow();
        visit.setDuration( visit.getCurrentDuration());
        visit.setCost( visit.calculateCost(pricePerMinute));
        visit.setFinished(true);
        table.setFree(true);
        return visit;
    }

    /**
     * Возвращает список свободных столов в ресторане.
     * @return список объектов Table, представляющих свободные столы
     */
    public static List<Table> getFreeTables(){
        return TableService.getFreeTables();
    }

    /**
     * Возвращает список зарезервированных столов в ресторане
     * @return список объектов Table, представляющих зарезервированные столы
     */
    public static List<Table> getReservedTables(){
        return TableService.getReservedTables();
    }

    /**
     * Получает текущую продолжительность посещения для указанного стола
     * @param tableId идентификатор стола, для которого нужно получить текущую продолжительность посещения
     * @return текущая продолжительность посещения в секундах
     */
    public static  long getCurrentDuration(int tableId){
        Visit visit = visits.stream()
                .filter(v -> v.getTable().getId() == tableId && !v.isFinished())
                .findFirst().orElseThrow();
        return visit.calculateDuration();
    }

    /**
     *  Получает общую текущую продолжительность посещения для всех зарезервированных столов
     * @return карта, где ключи - объекты столов, а значения - текущая продолжительность посещения в секундах
     */
    public static  Map<Table, Long> getTotalCurrentDuration() {
        Map<Table, Long> durations= new HashMap<>();
         for (Table table: TableService.getReservedTables() ) {
            durations.put(table, getCurrentDuration(table.getId()));
        }
            return durations;
    }

    /**
     * Получает текущую стоимость посещения для указанного стола
     * @param tableId идентификатор стола, для которого нужно получить текущую стоимость посещения
     * @return текущая стоимость посещения
     */
    public static  double getCurrentCost(int tableId){
        Visit visit = visits.stream()
                .filter(v -> v.getTable().getId() == tableId && !v.isFinished())
                .findFirst().orElseThrow();
        return visit.calculateCost(pricePerMinute);
    }

    /**
     * Получает общую текущую стоимость посещения для всех зарезервированных столов
     * @return карта, где ключи - объекты столов, а значения - текущая стоимость посещения.
     */
    public static  Map<Table, Double> getTotalCurrentCost() {
        Map<Table, Double> costs = new HashMap<>();
        for (Table table: TableService.getReservedTables() ) {
            costs.put(table, getCurrentCost(table.getId()));
        }
        return costs;
    }

    /**
     * Получает общую стоимость посещения для всех завершенных посещений
     * @return общая стоимость посещения всех завершенных посещений.
     */
    public static  double getTotalCostOfAllTime() {
        return visits.stream()
                .filter(Visit::isFinished).mapToDouble(Visit::getCost).sum();
    }

    /**
     * Получает среднюю продолжительность посещения для указанного стола
     * @param tableId тдентификатор стола, для которого нужно получить среднюю продолжительность посещения
     * @return средняя продолжительность посещения в секундах
     */

    public static long getAverageDurationOfTable(int tableId){
        int times = visits.stream()
                .filter(v -> v.getTable().getId() == tableId && v.isFinished()).toList().size();
       return visits.stream()
                .filter(v -> v.getTable().getId() == tableId && v.isFinished()).mapToLong(Visit::getDuration).sum() / times;

    }


    /**
     * Получает среднюю продолжительность посещения для всех столов
     * @return карта, где ключи - объекты столов, а значения - средняя продолжительность посещения в секундах
     */
    public static Map<Table, Long> getAverageDurationOfAllTables(){
        Map<Table, Long> averageDurations = new HashMap<>();
        for (Table table :TableService.tables.values()) {
            averageDurations.put(table, getAverageDurationOfTable(table.getId()));
        }
        return averageDurations;
    }
}
