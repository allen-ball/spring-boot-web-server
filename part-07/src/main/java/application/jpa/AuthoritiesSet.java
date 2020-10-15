package application.jpa;

import application.Authorities;
import java.util.TreeSet;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.NoArgsConstructor;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

@NoArgsConstructor
public class AuthoritiesSet extends TreeSet<Authorities> {
    @Converter(autoApply = true)
    @NoArgsConstructor
    public static class ConverterImpl implements AttributeConverter<AuthoritiesSet,String> {

        @Override
        public String convertToDatabaseColumn(AuthoritiesSet set) {
            return set.stream().map(t -> t.name()).collect(joining(","));
        }

        @Override
        public AuthoritiesSet convertToEntityAttribute(String string) {
            AuthoritiesSet set =
                Stream.of(string.split(","))
                .map(String::trim)
                .filter(t -> (! t.isEmpty()))
                .map(t -> Authorities.valueOf(t))
                .collect(toCollection(AuthoritiesSet::new));

            return set;
        }
    }
}
