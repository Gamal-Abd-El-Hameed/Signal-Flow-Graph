package au.ctrl.prjct;

import au.ctrl.prjct.graph.Edge;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class Config {

    @Bean
    public Map<String, Integer> nodes() {
        return new HashMap<>();
    }

    @Bean
    @Qualifier("edges")
    public List<List<Edge>> edges() {
        return new ArrayList<>();
    }

    @Bean
    @Qualifier("paths")
    public List<List<Edge>> paths() {
        return new ArrayList<>();
    }

    @Bean
    @Qualifier("loops")
    public List<List<Edge>> loops() {
        return new ArrayList<>();
    }
}
