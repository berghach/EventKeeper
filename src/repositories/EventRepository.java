package repositories;

import entities.Event;
import enums.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Date;

public class EventRepository implements Repository<Event> {
    private List<Event> events = new ArrayList<>();

    @Override
    public Event create(Event event) {
        events.add(event);
        return event;
    }

    @Override
    public Optional<Event> read(int id) {
        return events.stream()
                     .filter(event -> event.getId() == id)
                     .findFirst();
    }

    @Override
    public List<Event> readAll() {
        return new ArrayList<>(events);
    }

    @Override
    public Event update(Event event) {
        int index = events.indexOf(read(event.getId()).orElse(null));
        if (index >= 0) {
            events.set(index, event);
            return event;
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        return events.removeIf(event -> event.getId() == id);
    }
    // Search method with optional parameters
    public List<Event> search(Date date, String location, EventType type) {
        return events.stream()
            .filter(event -> (date == null || event.getDate().equals(date)))
            .filter(event -> (location == null || event.getLocation().equalsIgnoreCase(location)))
            .filter(event -> (type == null || event.getType() == type))
            .collect(Collectors.toList());
    }
}
