public class SubTask extends Task {
    private Epic EPIC;

    public void setToPlugEpic(Epic epic) {
        this.EPIC = epic;
    }

    public Epic getEpic() {
        return this.EPIC;
    }
}
