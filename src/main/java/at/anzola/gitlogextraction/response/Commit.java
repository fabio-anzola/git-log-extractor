package at.anzola.gitlogextraction.response;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The Commit class
 *
 * @author fabioanzola
 */
public class Commit {

    /**
     * Contains the hash of the commit
     */
    public String hash;

    /**
     * Contains the author of the commit
     */
    public String author;

    /**
     * Contains the commit date
     */
    public LocalDateTime authorDate;

    /**
     * Contains the commit title/message
     */
    public String message;

    /**
     * Constructor with title and full message
     *
     * @param hash The hash of the commit
     * @param author The author of the commit
     * @param authorDate The date of the commit
     * @param title The title of the commit
     * @param fullMessage The full message of the commit
     */
    public Commit(String hash, String author, LocalDateTime authorDate, String title, String fullMessage) {
        this.hash = hash;
        this.author = author;
        this.authorDate = authorDate;
        this.message = title + ": " + fullMessage;
    }

    /**
     * Constructor with title
     *
     * @param hash The hash of the commit
     * @param author The author of the commit
     * @param authorDate The date of the commit
     * @param title The title of the commit
     */
    public Commit(String hash, String author, LocalDateTime authorDate, String title) {
        this.hash = hash;
        this.author = author;
        this.authorDate = authorDate;
        this.message = title;
    }

    /**
     * The equal method for the Commit
     *
     * @param o The Commit to check if it is equal
     * @return If the Commits are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commit)) {
            return false;
        }
        Commit commit = (Commit) o;
        return hash.equals(commit.hash)
                && author.equals(commit.author)
                && authorDate.equals(commit.authorDate)
                && message.equals(commit.message);
    }

    /**
     * The hashcode method for the Commit
     *
     * @return The hashcode of the Commit
     */
    @Override
    public int hashCode() {
        return Objects.hash(hash, author, authorDate, message);
    }

    /**
     * The toString method for the Commit
     *
     * @return The formatted output for a Commit
     */
    @Override
    public String toString() {
        return "Commit{"
                + "hash='" + hash + '\''
                + ", author='" + author + '\''
                + ", authorDate=" + authorDate
                + ", message='" + message + '\''
                + '}';
    }

    /**
     * Gets the hash
     *
     * @return The Hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * Gets the Author
     *
     * @return The Author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the AuthorDate
     *
     * @return The AuthorDate
     */
    public LocalDateTime getAuthorDate() {
        return authorDate;
    }

    /**
     * Gets the Message
     *
     * @return The Message
     */
    public String getMessage() {
        return message;
    }
}
