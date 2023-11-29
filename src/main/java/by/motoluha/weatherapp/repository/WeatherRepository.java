package by.motoluha.weatherapp.repository;

import by.motoluha.weatherapp.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing Weather entities.
 */
@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    /**
     * Retrieves the last added weather entry.
     *
     * @return The last {@link Weather} entry added to the database.
     */
    Weather findFirstByOrderByIdDesc();

    /**
     * Retrieves a list of weather records within a specified date range.
     *
     * @param from Start date of the interval for lastUpdated field.
     * @param to   End date of the interval for lastUpdated field.
     * @return List of Weather objects that fall within the specified date range.
     */
    List<Weather> findByLastUpdatedBetween(LocalDate from, LocalDate to);
}
