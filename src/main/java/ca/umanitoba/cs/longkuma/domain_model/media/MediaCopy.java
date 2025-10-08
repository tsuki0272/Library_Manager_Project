package ca.umanitoba.cs.longkuma.domain_model.media;
import com.google.common.base.Preconditions;

public class MediaCopy {
    private int copyNumber;

    public MediaCopy(int copyNumber) {
        this.copyNumber = copyNumber;
        checkMediaCopy();
    }

    private void checkMediaCopy() {
        Preconditions.checkState(copyNumber >= 1, "Copy number should be at least 1.");
    }

    public int getCopyNumber() {
        checkMediaCopy();
        return copyNumber;
    }
}
