package dream.anywhere.Bean;

/**
 * Created by SKYMAC on 16/9/6.
 */
public class BuyFoodBean {
    private FoodBean food;
    private int sum;

    public BuyFoodBean(FoodBean food, int sum) {
        this.food = food;
        this.sum = sum;
    }

    public FoodBean getFood() {
        return food;
    }

    public void setFood(FoodBean food) {
        this.food = food;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "BuyFoodBean{" +
                "food=" + food +
                ", sum=" + sum +
                '}';
    }
}
