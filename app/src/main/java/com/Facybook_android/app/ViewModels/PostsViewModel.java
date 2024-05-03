    package com.Facybook_android.app.ViewModels;

    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.ViewModel;

    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Repository.repositories.PostsRepository;

    import java.util.List;

    public class PostsViewModel extends ViewModel {

        private PostsRepository mRepository;

        private LiveData<List<Post>> posts;

        public PostsViewModel () {
            mRepository = new PostsRepository();
            posts = mRepository.getAll();
        }

        public LiveData<List<Post>> get() { return posts; }

    //    public void add(Post post) { mRepository.add(post); };
    //
    //    public void delete(Post post) { mRepository.delete(post); }
    //
    //    public void reload() { mRepository.reload(); }
    }
