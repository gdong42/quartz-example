package info.donggan.blog.service;

import org.springframework.stereotype.Service;

/**
 * Date: 16/8/25
 * Time: 下午4:47
 *
 * @author Gan Dong
 */
@Service
public class FooService {

  public String getFoo() {
    return "foo";
  }

  public String getBar() {
    return "bar";
  }
}
