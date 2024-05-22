    package com.Facybook_android.app.View.Feed;

    import static com.Facybook_android.app.Context.MyApplication.context;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.EditText;
    import android.widget.ImageButton;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.room.Room;

    import com.Facybook_android.app.Context.MyApplication;
    import com.Facybook_android.app.Model.Feed.FeedModel;
    import com.Facybook_android.app.Model.adapters.CommentsListAdapter;
    import com.Facybook_android.app.Model.entities.Comment;
    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.Repository.AppDB;
    import com.Facybook_android.app.Repository.interfaces.CommentDao;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.ViewModels.CommentsViewModel;
    import com.Facybook_android.app.ViewModels.PostsViewModel;
    import com.Facybook_android.app.ViewModels.UsersViewModel;

    import java.util.ArrayList;
    import java.util.List;

    public class comments extends AppCompatActivity {
        private final FeedModel model = new FeedModel();
        private EditText editTxt;
        private User user;
        private RecyclerView lstComments;
        private List<Post> posts;
        private String postId;
        private Post post;
        private String LoggedInUser;
        private CommentsListAdapter adapter;
//        private List<Comment> comments;
        private PostsViewModel postsViewModel;
        private CommentsViewModel commentsViewModel;
        private UsersViewModel usersViewModel;
        private String publishersPic;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.comments_layout);
            postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
            commentsViewModel = new ViewModelProvider(this).get(CommentsViewModel.class);
            usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
            usersViewModel.getUser(LoggedInUser);

            postId = getIntent().getStringExtra("postId");
            LoggedInUser = getIntent().getStringExtra("LoggedInUser");
            commentsViewModel.getPostsComments(LoggedInUser, postId);

            initialize();
            setupButtons();
            observeCommentsViewModel();
        }

        private void setupButtons() {
            ImageButton sendBtn = findViewById(R.id.sendButton);
            sendBtn.setOnClickListener(v -> {
                if (posts != null && user != null) {
                    findPost();
                    model.addComment(publishersPic, editTxt, LoggedInUser, post, commentsViewModel);
                }
            });
        }

        private void findPost () {
            for ( Post post : posts) {
                if (post.getPid().equals(postId)) {
                    this.post = post;
                    break;
                }
            }
        }

        private void observePostsViewModel() {
            postsViewModel.get().observe(this, posts -> {
                Log.e("observer", "Observing posts list. Number of posts: " + (posts != null ? posts.size() : "null"));
                this.posts = posts;
            });
        }

        private void observeUsersViewModel() {
            usersViewModel.get().observe(this, user -> {
                this.user = user;
                Log.e("observer", "Observing users list");
                if (user != null && user.getName().equals(LoggedInUser)) {
                    this.publishersPic = user.getProfilePicture();
                }
            });
        }

        private void observeCommentsViewModel() {
            commentsViewModel.get().observe(this, comments -> {
                Log.e("observer", "Observing comments list. Number of comments: " + (comments != null ? comments.size() : "null"));
                adapter.setCommentsL(comments);
                observePostsViewModel();
                observeUsersViewModel();
            });
        }

        private void initialize() {
            editTxt = findViewById(R.id.commentEditText);
            lstComments = findViewById(R.id.lstComments);

            adapter = new CommentsListAdapter(this, commentsViewModel);
            lstComments.setAdapter(adapter);
            lstComments.setLayoutManager(new LinearLayoutManager(this));
        }



    }
