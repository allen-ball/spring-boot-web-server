package application.jpa;

import application.Authorities;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

@NoArgsConstructor
public class AuthoritiesSet extends TreeSet<Authorities> {
    public List<GrantedAuthority> asGrantedAuthorityList() {
        String[] strings = stream().map(Authorities::name).toArray(String[]::new);

        return AuthorityUtils.createAuthorityList(strings);
    }

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
                .filter(Predicate.not(String::isEmpty))
                .map(Authorities::valueOf)
                .collect(toCollection(AuthoritiesSet::new));

            return set;
        }
    }
}
