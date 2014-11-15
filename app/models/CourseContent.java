package models;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by cookie on 14.11.14.
 */
@Entity
public class CourseContent extends Model {

    @Id
    @Required
    public int id;
    public String courseName;
    public String contentName;
    public int contentId;
    public String pathFile;
    public String email;
    @Column(columnDefinition = "TEXT")
    public String content;
    public CourseContent(String courseName, String contentName, int id, String content, int contentId, String pathFile, String email ) {
        this.courseName = courseName;
        this.contentName = contentName;
        this.id = id;
        this.content = content;
        this.contentId = contentId;
        this.pathFile = pathFile;
        this.email = email;
    }


    public static Finder<String, CourseContent> find =
            new Finder<String, CourseContent>(String.class, CourseContent.class);
}