package mw.metrics;

import org.springframework.stereotype.Service;

@Service
public class MovieService {

    public String getFor(String name) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
        return "Test";
    }

    public String score(String name) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
        return name;
    }

}
