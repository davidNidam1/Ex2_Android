    package com.Facybook_android.app.ViewModels;

    import android.text.format.DateUtils;

    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.ViewModel;

    import com.Facybook_android.app.Model.entities.Comment;
    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Repository.repositories.PostsRepository;

    import java.util.List;
    import java.util.Timer;
    import java.util.TimerTask;

    public class PostsViewModel extends ViewModel {

        private PostsRepository mRepository;
        private LiveData<List<Post>> posts;

        public PostsViewModel() {
            mRepository = new PostsRepository();
            posts = mRepository.getAll();
        }

        public LiveData<List<Post>> get() { return posts; }
        public void getUsersPosts(String id) { mRepository.getUsersPosts(id); }
        public void add(Post post, String id) { mRepository.add(post, id); };
        public void reload() { mRepository.reload(); }
        public void reload(String id) { mRepository.reload(id); }
        public void update(String id, String pid, Post post) { mRepository.update(id, pid, post); }
        public void updateLikes(String id, String pid, Post post) { mRepository.updateLikes(id, pid, post); }
        public void delete(String publisher, String postId) {mRepository.delete(publisher, postId); }
        public void clear() { mRepository.clear(); }
    }
