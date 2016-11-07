package com.why.tool.random;

/** use the {@link com.why.tool.random.RandomUtil RandomUtil} instead */
@Deprecated
public interface ProbabilityGenerator {

    /**
     * 依据百分比返回真
     * 
     * @param percentage
     * @return true in random of percentage
     */
    boolean getRandomWithPercentage(float percentage);

    boolean getReandomBoolean();

    /**
     * 使用技能
     */
    public static final String EVENT_TYPE_USE_SKILL = "useSkill";

    /** 战败武将是否战死 */
    public static final String EVENT_TYPE_SHALL_DEFEATED_OFFICER_DIE = "shallDefeatedOfficerDie";

    /** 武将使用击兵技 */
    public static final String EVENT_TYPE_USE_FIGHTING_SKILL = "useFightingSkill";

    /** 装饰产生产出的类型是哪一种 */
    public static final String EVENT_TYPE_BUILDING_ITEM_OUTPUT_TYPE = "buildingItemOutputType";

    /**
     * 产生0到max之间的整数，包括max，概率平均 例如max=3，则产生0,1,2,3这四个数字的概率均为25% [min, max]
     * 
     * @param max
     * @return
     */
    int getRandomNumber(int max);

    /**
     * 产生min到max之间的整数
     * 
     * @param min
     * @param max
     * @return
     */
    int getRandomNumber(int min, int max);

    /**
     * 产生概率区间（总概率必须为100%）
     * 
     * @param percentArr
     *            概率数组，和为100
     * @return
     */
    int getRandomChoiceWithPercentArr(int[] percentArr);

    int getRandomChoiceWithPermillageArr(int[] permililageArr);

    /**
     * 产生概率区间
     * 
     * @param ratios
     *            概率权重数组，和可以不为100；但产生的概率的和为100
     * @return
     */
    int getRandomChoiceWithRatioArr(int[] ratios);

    /** 等价于Math.random or random.nextFloat 产生[0,1) */
    float nextFloat();

    int getAverageChoiceIndex(float[] percentageArr);

    int getRandomChoice(int[] choiceArr);
    
    public int randomBasedOnWeight(int[] weights);
}
