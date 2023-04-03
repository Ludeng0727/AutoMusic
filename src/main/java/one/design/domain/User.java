package one.design.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String userId;
    String pw;
    String nickname;

    public User(String userId, String pw, String nickname) {
        this.userId = userId;
        this.pw = pw;
        this.nickname = nickname;
    }
}
