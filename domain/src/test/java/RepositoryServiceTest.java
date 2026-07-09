import com.example.api.port.out.GitHubRepositoryProvider;
import com.example.api.port.out.RepositoryProvider;
import com.example.domain.RepositoryService;
import com.example.model.exception.RepositoryNotFoundException;
import com.example.model.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RepositoryServiceTest {

    private GitHubRepositoryProvider gitHubRepositoryProvider;
    private RepositoryProvider repositoryProvider;
    private RepositoryService repositoryService;

    @BeforeEach
    void setUp() {
        gitHubRepositoryProvider = mock(GitHubRepositoryProvider.class);
        repositoryProvider = mock(RepositoryProvider.class);
        repositoryService = new RepositoryService(gitHubRepositoryProvider, repositoryProvider);
    }

    @Test
    void getRepository_DataCorrect_ReturnRepository() {
        Repository repository = new Repository("octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(repositoryProvider.findByOwnerAndRepositoryName("octocat", "Hello-World")).thenReturn(Optional.of(repository));

        Repository result = repositoryService.getRepository("octocat", "Hello-World");

        assertEquals("octocat", result.getOwner());
        assertEquals("Hello-World", result.getRepositoryName());
        assertEquals("octocat/Hello-World", result.getFullName());
        assertEquals("My first repository on GitHub!", result.getDescription());
        assertEquals("https://github.com/octocat/Hello-World.git", result.getCloneUrl());
        assertEquals(3572L, result.getWatchers());
        assertEquals(LocalDateTime.of(2011, 1, 26, 19, 1, 12), result.getCreatedAt());
    }

    @Test
    void createRepository_DataCorrect_ReturnRepository() {
        Repository repository = new Repository("octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(gitHubRepositoryProvider.getRepository("octocat", "Hello-World")).thenReturn(repository);
        when(repositoryProvider.save(repository)).thenReturn(repository);

        Repository result = repositoryService.createRepository("octocat", "Hello-World");

        assertEquals(repository.getFullName(), result.getFullName());
        verify(repositoryProvider).save(repository);
    }

    @Test
    void getRepository_RepositoryNotFound_ThrowsException() {
        when(repositoryProvider.findByOwnerAndRepositoryName("octocat", "Hello-World")).thenReturn(Optional.empty());

        assertThrows(
                RepositoryNotFoundException.class,
                () -> repositoryService.getRepository("octocat", "Hello-World")
        );
    }

    @Test
    void createRepository_RepositoryReturnedByGithub_SavedInRepository() {
        Repository repository = new Repository("octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(gitHubRepositoryProvider.getRepository("octocat", "Hello-World")).thenReturn(repository);
        when(repositoryProvider.save(any())).thenReturn(repository);

        repositoryService.createRepository("octocat", "Hello-World");

        verify(repositoryProvider).save(repository);
    }

    @Test
    void updateRepository_DataCorrect_ReturnUpdatedRepository() {
        Repository repository = new Repository("octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        Repository updated = new Repository("octocat", "Hello-World", "octocat/Hello-World", "Updated description", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(repositoryProvider.findByOwnerAndRepositoryName("octocat", "Hello-World")).thenReturn(Optional.of(repository));
        when(gitHubRepositoryProvider.getRepository("octocat", "Hello-World")).thenReturn(updated);
        when(repositoryProvider.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Repository result = repositoryService.updateRepository("octocat", "Hello-World");

        assertEquals("Updated description", result.getDescription());
    }

    @Test
    void deleteRepository_RepositoryExists_DeleteCalled() {
        Repository repository = new Repository("octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(repositoryProvider.findByOwnerAndRepositoryName("octocat", "Hello-World")).thenReturn(Optional.of(repository));

        repositoryService.deleteRepository("octocat", "Hello-World");

        verify(repositoryProvider).deleteByOwnerAndRepositoryName("octocat", "Hello-World");
    }
}