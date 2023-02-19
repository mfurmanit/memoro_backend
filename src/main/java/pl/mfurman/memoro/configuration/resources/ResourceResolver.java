package pl.mfurman.memoro.configuration.resources;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

public class ResourceResolver extends PathResourceResolver {

  @Override
  protected Resource getResource(@NonNull final String resourcePath,
                                 final Resource location) throws IOException {
    final Resource requestedResource = location.createRelative(resourcePath);
    return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
      : new ClassPathResource("/static/index.html");
  }
}
