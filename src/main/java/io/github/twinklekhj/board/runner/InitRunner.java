package io.github.twinklekhj.board.runner;

import io.github.twinklekhj.board.dao.entity.Board;
import io.github.twinklekhj.board.dao.entity.Member;
import io.github.twinklekhj.board.dao.repository.board.BoardRepository;
import io.github.twinklekhj.board.dao.repository.member.MemberRepository;
import io.github.twinklekhj.board.login.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitRunner implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Member member = Member.builder()
                .memberId("admin")
                .name("관리자")
                .password("admin")
                .email("admin@domain.com")
                .role(Role.ADMIN)
                .build();

        memberRepository.save(member);

        List<Board> boards = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            boards.add(Board.builder()
                    .member(member)
                    .title("Test " + i)
                    .content("Content " + i).build());
        }
        boardRepository.saveAll(boards);

        Board posting = Board.builder()
                .member(member)
                .title("Lorem Ipsum")
                .content("""
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sit amet volutpat tellus. Fusce nec massa eu ex porttitor porttitor at sit amet dui. Nulla scelerisque ipsum magna, ac condimentum leo luctus vitae. Pellentesque consectetur in nunc sed commodo. Interdum et malesuada fames ac ante ipsum primis in faucibus. In a lacinia turpis, ut aliquet justo. Suspendisse consequat mauris ac ligula placerat suscipit. Praesent vitae porta diam, semper ultrices quam. Lorem ipsum dolor sit amet, consectetur adipiscing elit. In velit tortor, lobortis a sollicitudin non, tincidunt et felis. Suspendisse scelerisque sollicitudin tortor. Sed porta, quam et dictum interdum, nisi neque feugiat lacus, et suscipit odio neque venenatis dolor. Sed scelerisque pulvinar ante, eu ullamcorper ligula suscipit congue. Etiam accumsan dapibus ante, non venenatis metus viverra eget. In luctus ligula elit, quis rutrum purus ultricies id.</p>
                                                
                        <br>
                                                
                        <p>Curabitur dapibus tincidunt purus, quis luctus felis. Suspendisse tincidunt tristique dolor, vitae aliquet magna feugiat nec. Curabitur quis neque arcu. Integer quis pharetra turpis. Vivamus elementum neque ut lectus interdum, non interdum justo lobortis. Nulla fermentum pellentesque lorem, id commodo ante feugiat id. Aenean vulputate metus nec felis fermentum iaculis.</p>
                                                
                        <br>
                                                
                        <p>Fusce mattis sapien at pellentesque porttitor. Donec cursus, dui id pulvinar ornare, metus diam auctor est, eu dignissim lorem erat sed sapien. Ut ultricies ultricies rutrum. Phasellus luctus fringilla felis. Etiam arcu massa, mattis sed odio at, facilisis placerat ex. Vestibulum at libero felis. Vestibulum sit amet tortor ornare, accumsan ex lobortis, placerat lectus. In id venenatis urna. Fusce eleifend ornare urna, a imperdiet ante consequat a.</p>
                                                
                        <br>
                                                
                        <p>Phasellus rutrum sit amet odio ac dapibus. Aenean tristique sem id blandit congue. Pellentesque lacus nunc, convallis vitae tellus vel, feugiat laoreet tellus. Nulla non nulla dui. Praesent blandit sit amet orci id condimentum. Etiam nec blandit nulla. In a mauris posuere lorem finibus tristique et ut urna. Aenean porta nunc pellentesque nunc malesuada euismod. Etiam id fermentum augue. Pellentesque eget libero metus. Morbi et lectus quis odio luctus iaculis. Aliquam velit metus, blandit non consequat id, hendrerit quis leo. Fusce tristique mauris velit, a scelerisque dui aliquet eget. Duis bibendum, nulla posuere ultricies feugiat, risus arcu lobortis sapien, sed placerat quam ante id mauris. Suspendisse potenti.</p>
                                                
                        <br>
                                                
                        <p>Aenean rhoncus laoreet lectus. Morbi pharetra nibh eu elementum scelerisque. In auctor varius accumsan. Mauris lacinia malesuada justo. Sed scelerisque augue eu commodo malesuada. Suspendisse id convallis dui, a pharetra lacus. Ut pellentesque ut orci sed ullamcorper. Maecenas a luctus ex. Quisque eu porttitor tellus, vitae laoreet elit. Vestibulum nulla ligula, ultrices at metus sed, facilisis sodales nulla. Quisque tellus eros, porttitor id magna id, congue fringilla lacus. Sed vel auctor libero.</p>
                        """)
                .build();
        boardRepository.save(posting);
    }
}
