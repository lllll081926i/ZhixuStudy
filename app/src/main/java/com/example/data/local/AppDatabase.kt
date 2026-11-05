package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        QuestionBankEntity::class,
        QuestionEntity::class,
        PracticeRecordEntity::class,
        ImportPresetEntity::class,
        ImportLogEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "zhixu_serene_study.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

object SeedDataFactory {
    fun initialBanks(): List<QuestionBankEntity> = listOf(
        QuestionBankEntity(
            name = "《铁路运输组织基础》",
            subtitle = "车站工作 · 车流组织 · 列车运行图",
            domainTag = "railway",
            totalTargetCount = 186,
            practicedCount = 64,
            accuracyPercent = 78,
            versionTag = "v2.4",
            updatedAt = System.currentTimeMillis()
        ),
        QuestionBankEntity(
            name = "《公路交通工程》",
            subtitle = "交通流理论 · 道路几何设计 · 信号配时",
            domainTag = "highway",
            totalTargetCount = 92,
            practicedCount = 26,
            accuracyPercent = 82,
            versionTag = "v1.8",
            updatedAt = System.currentTimeMillis() - 3600_000L
        ),
        QuestionBankEntity(
            name = "《模拟测试题集》",
            subtitle = "历年真题精编 · 高频考点冲刺",
            domainTag = "exam",
            totalTargetCount = 50,
            practicedCount = 18,
            accuracyPercent = 74,
            versionTag = "v3.0",
            updatedAt = System.currentTimeMillis() - 7200_000L
        ),
        QuestionBankEntity(
            name = "《高数与计算机体系结构》",
            subtitle = "流水线微架构 · 泰勒展开 · 线性代数",
            domainTag = "cs_math",
            totalTargetCount = 60,
            practicedCount = 22,
            accuracyPercent = 71,
            versionTag = "v2.1",
            updatedAt = System.currentTimeMillis() - 10800_000L
        )
    )

    fun initialQuestions(): List<QuestionEntity> = listOf(
        // 1. Railway Single Choice
        QuestionEntity(
            id = "Q001",
            bank = "《铁路运输组织基础》",
            chapter = "第二章：列车运行图与区间通过能力",
            section = "列车运行图基础",
            type = "single",
            difficulty = "简单",
            score = 2.0,
            stem = "铁路运输组织中列车运行图的基本作用是什么？",
            optionsJson = listToJson(
                listOf(
                    "A. 全路组织列车运行的基础，综合性作业规划纲领",
                    "B. 仅用于客运车站时刻表的制定依据",
                    "C. 单纯记录机车车辆检修周期的统计表格",
                    "D. 仅供调度员临时调整车流的参考草图"
                )
            ),
            answersJson = listToJson(listOf("A")),
            explanation = "列车运行图是全路组织列车运行的基础，它将铁路各部门（机务、车辆、工务、电务、车务）的工作有机联系起来，是铁路运输生产的核心综合规划。",
            trapAnalysis = "选项B将其范围窄化为仅限客运车站时刻表；旅客列车时刻表只是运行图对外公布的一部分。",
            mnemonic = "运行图 = 全路行车总纲领 + 部门协同时间表",
            wrongCount = 0,
            isStarred = false,
            masteryPercent = 88,
            lastPracticedLabel = "昨日"
        ),
        // 2. Railway Multiple Choice
        QuestionEntity(
            id = "Q002",
            bank = "《铁路运输组织基础》",
            chapter = "第二章：列车运行图与区间通过能力",
            section = "运行图要素",
            type = "multiple",
            difficulty = "中等",
            score = 3.0,
            stem = "列车运行图能够清晰反映并规定下列哪些行车要素与时空关系？",
            optionsJson = listToJson(
                listOf(
                    "A. 列车占用区间的程序与次序",
                    "B. 列车在每个车站的到达、出发或通过时刻",
                    "C. 列车在区间的运行时分与在车站的停站时分",
                    "D. 车站货场叉车日常充电维护排班表"
                )
            ),
            answersJson = listToJson(listOf("A", "B", "C")),
            explanation = "列车运行图以横轴表示时间、纵轴表示距离（车站中心线），直接规定了列车占用区间次序、各站到发通过时刻、区间运行时分、车站停站时分以及机车交路与重量长度标准。",
            trapAnalysis = "选项D属于车站装卸机械内部日常维保细节，不在列车运行图规定范畴内。",
            mnemonic = "占用次序 + 到发时刻 + 运行与停站时分",
            wrongCount = 1,
            isStarred = true,
            masteryPercent = 72,
            lastPracticedLabel = "2天前",
            lastWrongAnswer = "A,B"
        ),
        // 3. Railway Judge (True/False)
        QuestionEntity(
            id = "Q003",
            bank = "《铁路运输组织基础》",
            chapter = "第三章：车流组织与编组计划",
            section = "空重车流调配",
            type = "judge",
            difficulty = "简单",
            score = 1.5,
            stem = "铁路车流组织中，重车流与空车流必须严格按照完全相同的列车编组计划组织挂运。",
            optionsJson = listToJson(listOf("正确", "错误")),
            answersJson = listToJson(listOf("错误")),
            explanation = "重车流严格按照列车编组计划和车流径路组织运输；而空车流主要根据装车地需求与调度排空指令组织调配（如专列排空或顺路捎带），两者遵循的组织原则并不完全相同。",
            trapAnalysis = "最近错误选择「正确」，需留意：空车流有专门的输送调配径路指令，不机械套用重车流编组。",
            mnemonic = "重车按编组计划，空车按排空调整指令",
            wrongCount = 1,
            isStarred = false,
            masteryPercent = 60,
            lastPracticedLabel = "1天前",
            lastWrongAnswer = "正确"
        ),
        // 4. Railway Single Choice (Conflict Item A1 - matches Q014 in Exam bank with different answer for Audit!)
        QuestionEntity(
            id = "Q004",
            bank = "《铁路运输组织基础》",
            chapter = "第一章：车站工作组织",
            section = "接发列车作业",
            type = "single",
            difficulty = "中等",
            score = 2.0,
            stem = "车站接发列车工作必须首先保证什么？",
            optionsJson = listToJson(
                listOf(
                    "A. 行车安全与列车按运行图正点运行",
                    "B. 车站调车作业效率最大化",
                    "C. 货运装卸作业优先完成",
                    "D. 减少道岔转换次数"
                )
            ),
            answersJson = listToJson(listOf("A")),
            explanation = "根据《技规》及铁路运输组织原则，车站接发列车工作必须坚持安全第一，首先保证行车安全，并严格按列车运行图接发列车。",
            trapAnalysis = "调车作业不得影响接发列车进路准备与行车安全。",
            mnemonic = "接发列车安全第一，调车让行列车",
            wrongCount = 2,
            isStarred = true,
            masteryPercent = 54,
            lastPracticedLabel = "2天前",
            lastWrongAnswer = "B"
        ),
        // 5. Railway Conflict Item A2 (for Full Library Audit study_4 / study_18)
        QuestionEntity(
            id = "Q005",
            bank = "《铁路运输组织基础》",
            chapter = "第一章：车站工作组织",
            section = "轻型轨道车行车",
            type = "single",
            difficulty = "困难",
            score = 2.0,
            stem = "在双线区间，仅限于昼间运行的轻型轨道车按什么行车制运行？",
            optionsJson = listToJson(
                listOf(
                    "A. 按列车办理，凭地面信号机显示运行",
                    "B. 按自动闭塞追踪法行车",
                    "C. 按电话闭塞法行车",
                    "D. 按施工封锁区间移动作业车办理"
                )
            ),
            answersJson = listToJson(listOf("A")),
            explanation = "在《铁路运输组织基础》早期教材口径中，按列车办理凭信号显示；但在《技规》第372条最新修订中，轻型车辆仅限昼间运行且不按列车办理，须填写轻型车辆使用书。",
            trapAnalysis = "极易混淆轻型车辆（不按列车办理）与重型轨道车（按列车办理）的行车凭证差异！",
            mnemonic = "重型轨道车按列车，轻型车辆填使用书",
            wrongCount = 3,
            isStarred = true,
            isFlagged = true,
            masteryPercent = 42,
            lastPracticedLabel = "今日"
        ),
        // 6. Railway Single Choice - Interval Carrying Capacity (study_7 / study_24)
        QuestionEntity(
            id = "Q006",
            bank = "《铁路运输组织基础》",
            chapter = "第二章：列车运行图与区间通过能力",
            section = "平行运行图通过能力",
            type = "single",
            difficulty = "中等",
            score = 2.0,
            stem = "在单线成对非追踪平行运行图上，决定区间通过能力大小的核心运行图周期要素是什么？",
            optionsJson = listToJson(
                listOf(
                    "A. 区间往返运行时分之和与车站间隔时间之和（T周 = t往 + t返 + τ站）",
                    "B. 仅取决于机车在折返段整备作业时间",
                    "C. 取决于沿线货运站平均装卸作业时间",
                    "D. 仅取决于旅客列车的最高设计时速"
                )
            ),
            answersJson = listToJson(listOf("A")),
            explanation = "单线成对非追踪平行运行图的周期 T周 由上下行纯运行时分（t往 + t返）、起停车附加时分以及车站会车/不同时到达间隔时间（τ）共同决定，最困难区间的 T周 决定了全区段限制通过能力。",
            trapAnalysis = "注意区分单线非追踪运行图周期与双线自动闭塞追踪间隔时间 I 的公式区别。",
            mnemonic = "N = (1440 - t天窗) × γ / T周",
            formulaHint = "N = \\frac{(1440 - t_{天窗}) \\cdot \\gamma}{t_{往} + t_{返} + \\tau_{会} + \\tau_{不}}",
            wrongCount = 1,
            isStarred = true,
            masteryPercent = 64,
            lastPracticedLabel = "2天前"
        ),
        // 7. Railway Fill-in-the-blank (study_9)
        QuestionEntity(
            id = "Q007",
            bank = "《铁路运输组织基础》",
            chapter = "第二章：列车运行图与区间通过能力",
            section = "通过能力计算",
            type = "fill",
            difficulty = "中等",
            score = 3.0,
            stem = "在计算铁路区间通过能力时，扣除综合维修天窗时间后，一昼夜可用时间与运行图周期的比值需乘以 [ 空位 1 ] 系数；而将非平行运行图上的旅客列车折算为普通货物列车时，所减少的货物列车数与开行旅客列车数之比称为 [ 空位 2 ] 。",
            optionsJson = listToJson(listOf("空位 1：区间通过能力折减/有效利用系数", "空位 2：扣除系数")),
            answersJson = listToJson(listOf("有效度系数", "扣除系数")),
            explanation = "一昼夜1440分钟扣除天窗时间后，还需考虑行车零星干扰的储备，即乘以有效度系数 γ（通常取0.92~0.96）；在非平行运行图中，每开行一列旅客列车（或快运列车）所占用的运行图能力相当于 ε 列普通货物列车，ε 即为扣除系数。",
            trapAnalysis = "空位1填写「有效度系数」或「有效利用系数」均判对；空位2务必准确填写标准术语「扣除系数」。",
            mnemonic = "一有效二扣除，天窗减完乘伽马",
            wrongCount = 1,
            isStarred = false,
            masteryPercent = 68,
            lastPracticedLabel = "3天前"
        ),
        // 8. Railway Comprehensive Calculation (study_26 - Hump Retarder Calculation with Scratchpad)
        QuestionEntity(
            id = "Q008",
            bank = "《铁路运输组织基础》",
            chapter = "第一章：车站工作组织",
            section = "驼峰溜放与减速器制动能高",
            type = "calc",
            difficulty = "困难",
            score = 10.0,
            stem = "【综合计算分析题】某机械化驼峰调车场推送难行车（m = 84t，基本阻力 ω₀ = 2.5 N/kN，风阻力 ωw = 1.2 N/kN）经峰顶溜放至调车线警冲标后计算点，峰高 H = 3.65m，溜放总走行距离 L = 320m，曲线及道岔局部附加折算能高 Σhc = 0.26m。试求：(1) 难行车全旅途总阻力消耗能高 Σhw；(2) 若要求到达计算点安全连挂速度 v末 ≤ 1.4 m/s，第一、二制动位减速器至少需消除的制动能高 hz（精确到 0.01m）。",
            optionsJson = listToJson(
                listOf(
                    "步骤 (1) 总阻力消耗能高 Σhw (单位: m)",
                    "步骤 (2) 减速器需消除制动能高 hz (单位: m)"
                )
            ),
            answersJson = listToJson(listOf("1.44", "2.11")),
            explanation = "【权威分步演算】\n1. 计算单位总阻力：ω总 = ω₀ + ωw = 2.5 + 1.2 = 3.7 N/kN（即 3.7‰）。\n2. 计算沿途基本与风阻消耗能高：hw0 = L × ω总 / 1000 = 320 × 3.7 / 1000 = 1.184m。\n3. 加上道岔与曲线局部阻力能高：Σhw = 1.184 + 0.26 = 1.444m ≈ 1.44m。\n4. 末速度对应的动能高：hv = v末² / (2g') = 1.4² / (2 × 9.6) ≈ 0.10m。\n5. 减速器需消除制动能高：hz = H - Σhw - hv = 3.65 - 1.44 - 0.10 = 2.11m。",
            trapAnalysis = "注意考虑车轮旋转惯性质量折算系数后有效重力加速度 2g' ≈ 19.2 m/s²，切勿漏加道岔曲线局部附加能高 0.26m。",
            mnemonic = "峰高H = 阻力能高Σhw + 制动能高hz + 剩余动能高hv",
            formulaHint = "h_z = H - \\left( \\frac{L(\\omega_0 + \\omega_w)}{1000} + \\sum h_c \\right) - \\frac{v_{末}^2}{2g'}",
            referenceTableJson = "难行车质量:84t|基本阻力 ω₀:2.5 N/kN|风阻力 ωw:1.2 N/kN|设计峰高 H:3.65 m|溜放距离 L:320 m|局部阻力 Σhc:0.26 m",
            wrongCount = 2,
            isStarred = true,
            isFlagged = true,
            masteryPercent = 45,
            lastPracticedLabel = "昨日"
        ),
        // 9. Highway Traffic Engineering - Single Choice
        QuestionEntity(
            id = "Q009",
            bank = "《公路交通工程》",
            chapter = "第一章：交通流理论与通行能力",
            section = "交通流三参数",
            type = "single",
            difficulty = "简单",
            score = 2.0,
            stem = "在格林希尔治（Greenshields）线性速度-密度模型中，当道路交通量 Q 达到最大通行能力 Qm 时，对应的最佳车流密度 Km 与阻塞密度 Kj 的关系是什么？",
            optionsJson = listToJson(
                listOf(
                    "A. Km = 0.5 Kj（阻塞密度的一半）",
                    "B. Km = 0.368 Kj（阻塞密度的 1/e）",
                    "C. Km = Kj（等于阻塞密度）",
                    "D. Km = 0.25 Kj"
                )
            ),
            answersJson = listToJson(listOf("A")),
            explanation = "格林希尔治模型假设速度 V 与密度 K 呈线性关系：V = Vf(1 - K/Kj)。流量 Q = K·V = Vf(K - K²/Kj)，对 K 求导令 dQ/dK = 0 可得最佳密度 Km = Kj / 2，最佳速度 Vm = Vf / 2，最大流量 Qm = Vf·Kj / 4。",
            trapAnalysis = "选项B（Km = Kj/e）是格林伯格（Greenberg）对数模型的结论，切勿将线性模型与对数模型混淆。",
            mnemonic = "格林希尔治线性折半：Km = Kj/2, Vm = Vf/2, Qm = Vf·Kj/4",
            wrongCount = 0,
            isStarred = true,
            masteryPercent = 85,
            lastPracticedLabel = "3天前"
        ),
        // 10. Highway Traffic Engineering - Multiple Choice
        QuestionEntity(
            id = "Q010",
            bank = "《公路交通工程》",
            chapter = "第二章：道路几何设计与视距",
            section = "平面曲线超高与加宽",
            type = "multiple",
            difficulty = "中等",
            score = 3.0,
            stem = "公路平曲线设置超高（Superelevation）与加宽的主要力学与几何作用包括哪些？",
            optionsJson = listToJson(
                listOf(
                    "A. 利用车辆重力沿横向坡面的分力抵消部分离心力",
                    "B. 减小横向力系数 μ，提高高速转弯时的横向抗滑稳定性与乘坐舒适性",
                    "C. 补偿车辆在曲线上行驶时后轮内移引起的有效轨迹宽度增加",
                    "D. 完全消除汽车转弯时的空气气动升力"
                )
            ),
            answersJson = listToJson(listOf("A", "B", "C")),
            explanation = "平曲线超高通过内倾横坡 ih 分担部分离心力，使横向力系数 μ = V²/(127R) - ih 控制在安全舒适范围内；平曲线加宽则是由于汽车前外轮与后内轮轨迹半径差导致有效占道变宽。",
            trapAnalysis = "超高无法消除空气气动升力，选项D明显错误。",
            mnemonic = "超高抗离心降μ，加宽补轨迹半径差",
            wrongCount = 1,
            isStarred = false,
            masteryPercent = 76,
            lastPracticedLabel = "4天前"
        ),
        // 11. Math & CS Architecture - Taylor Expansion (from Review Screen study_3)
        QuestionEntity(
            id = "Q011",
            bank = "《高数与计算机体系结构》",
            chapter = "高等数学 · 泰勒展开式",
            section = "麦克劳林展开",
            type = "single",
            difficulty = "困难",
            score = 3.0,
            stem = "函数 f(x) = ln(1+x) 在 x = 0 处的 3 阶带皮亚诺余项泰勒展开式中，x³ 的系数与余项阶数分别是多少？",
            optionsJson = listToJson(
                listOf(
                    "A. 系数为 +1/3，余项为 o(x³)",
                    "B. 系数为 -1/3，余项为 o(x³)",
                    "C. 系数为 +1/6，余项为 o(x³)",
                    "D. 系数为 -1/6，余项为 o(x⁴)"
                )
            ),
            answersJson = listToJson(listOf("A")),
            explanation = "ln(1+x) = x - x²/2 + x³/3 - x⁴/4 + ... + (-1)^(n-1) xⁿ/n + o(xⁿ)。当展开到 n=3 时，奇数次幂符号为正，因此 x³ 的系数为 +1/3（注意分母是 n 而不是 n!），皮亚诺余项为 o(x³)。",
            trapAnalysis = "极易与 e^x 或 sin(x) 的阶乘分母 3! = 6 混淆，或者把奇偶项正负号记反！",
            mnemonic = "ln(1+x) 分母无阶乘，奇正偶负交替排",
            wrongCount = 3,
            isStarred = true,
            isFlagged = true,
            masteryPercent = 48,
            lastPracticedLabel = "2天前",
            lastWrongAnswer = "C"
        ),
        // 12. Math & CS Architecture - Linear Algebra (from Review Screen study_3)
        QuestionEntity(
            id = "Q012",
            bank = "《高数与计算机体系结构》",
            chapter = "线性代数 · 特征值与特征向量",
            section = "实对称矩阵正交化",
            type = "single",
            difficulty = "中等",
            score = 3.0,
            stem = "设矩阵 A 为 3 阶实对称矩阵，已知其有两个重特征值 λ₁ = λ₂ = 2，另一特征值 λ₃ = -1 对应特征向量 α₃ = (1, 1, 1)ᵀ，则属于特征值 2 的线性无关特征向量满足什么充要条件？",
            optionsJson = listToJson(
                listOf(
                    "A. 与 α₃ 正交，即满足方程 x₁ + x₂ + x₃ = 0 的两个线性无关解向量",
                    "B. 与 α₃ 平行，即 α₃ 的非零常数倍",
                    "C. 满足 x₁ - x₂ + x₃ = 2 的任意向量",
                    "D. 必须为单位对角向量"
                )
            ),
            answersJson = listToJson(listOf("A")),
            explanation = "实对称矩阵不同特征值对应的特征向量必相互正交！因此属于重特征值 λ=2 的特征子空间与 α₃=(1,1,1)ᵀ 正交，直接由内积 (x, α₃) = x₁ + x₂ + x₃ = 0 即可写出两个线性无关特征向量，例如 (-1,1,0)ᵀ 与 (-1,0,1)ᵀ。",
            trapAnalysis = "无需先求矩阵 A 本身，直接利用实对称矩阵不同特征值特征向量正交即可秒杀。",
            mnemonic = "实对称不同特征值必正交，内积为零直接解",
            wrongCount = 2,
            isStarred = true,
            masteryPercent = 62,
            lastPracticedLabel = "昨日",
            lastWrongAnswer = "C"
        ),
        // 13. Computer Architecture - Pipeline Hazard (from Tablet study_25)
        QuestionEntity(
            id = "Q013",
            bank = "《高数与计算机体系结构》",
            chapter = "模块 04 · 计算机体系结构",
            section = "乱序执行与流水线冲突",
            type = "multiple",
            difficulty = "困难",
            score = 3.0,
            stem = "关于现代微处理器体系结构中的流水线冲突（Pipeline Hazard）分类及其在乱序执行引擎（OoO Core）中的化解机制，下列哪些表述是正确的？",
            optionsJson = listToJson(
                listOf(
                    "A. 结构冲突（Structural Hazard）：当硬件资源不足（如单个存储器端口冲突）时引发，可通过复制功能部件或采用哈佛架构分离指令与数据缓存来消除。",
                    "B. 写后读（RAW）：属于真数据相关（True Data Dependency）。乱序执行引擎无法通过寄存器重命名消除此类相关，只能依靠前递旁路（Forwarding）或等待操作数就绪。",
                    "C. 读后写（WAR）与写后写（WAW）：属于名称相关（Name Dependency），在现代超标量架构中可通过 Tomasulo 算法或物理寄存器重命名（RAT）彻底消除。",
                    "D. 控制冲突（Control Hazard）：仅出现在无条件跳转指令中，动态双模态分支预测器能够做到 100% 零开销消除投机错误带来的冲刷。"
                )
            ),
            answersJson = listToJson(listOf("A", "B", "C")),
            explanation = "RAW 是数据流动必需的因果链（真相关），只能靠旁路前递减少气泡；而 WAR（反相关）与 WAW（输出相关）仅因逻辑寄存器名字有限而产生，通过物理寄存器文件（PRF）与寄存器别名表（RAT）重命名即可彻底消除。",
            trapAnalysis = "选项D存在绝对化错误：分支预测算法（包括 TAGE 与两级自适应预测）只能逼近极高命中率，误预测时 ROB 仍需回滚冲刷流水线。",
            mnemonic = "RAW真相关靠旁路，WAR/WAW假相关靠重命名",
            wrongCount = 1,
            isStarred = true,
            isFlagged = true,
            masteryPercent = 70,
            lastPracticedLabel = "今日"
        ),
        // 14. Exam Bank - Intentional Cross-Bank Conflict Partner for Q005 (study_4 / study_18)
        QuestionEntity(
            id = "Q014",
            bank = "《模拟测试题集》",
            chapter = "第一部分：行车规章高频真题",
            section = "轻型车辆与轨道车",
            type = "single",
            difficulty = "困难",
            score = 2.0,
            stem = "在双线区间，仅限于昼间运行的轻型轨道车按什么行车制运行？",
            optionsJson = listToJson(
                listOf(
                    "A. 按列车办理，凭地面信号机显示运行",
                    "B. 不按列车办理，按轻型车辆承认手续凭使用书运行",
                    "C. 按电话闭塞法行车",
                    "D. 按自动站间闭塞行车"
                )
            ),
            answersJson = listToJson(listOf("B")),
            explanation = "根据《铁路技术管理规程》（普速铁路部分）第372条：轻型车辆是指由随乘人员能随时撤出线路外的轻型轨道车及其他非机动轻型车辆。轻型车辆仅限昼间运行，不按列车办理，须承认轻型车辆使用书。",
            trapAnalysis = "与《铁路运输组织基础》旧版题库 Q005 存在跨库答案冲突（A vs B），建议在题库体检中统一修订为 B。",
            mnemonic = "轻型车辆随时撤出线路，昼间运行填使用书",
            wrongCount = 2,
            isStarred = true,
            masteryPercent = 58,
            lastPracticedLabel = "今日"
        ),
        // 15. Exam Bank - Intentional Cross-Bank Conflict Partner for Q004
        QuestionEntity(
            id = "Q015",
            bank = "《模拟测试题集》",
            chapter = "第一部分：行车规章高频真题",
            section = "车站接发车安全",
            type = "single",
            difficulty = "中等",
            score = 2.0,
            stem = "车站接发列车工作必须首先保证什么？",
            optionsJson = listToJson(
                listOf(
                    "A. 行车安全与列车按运行图正点运行",
                    "B. 车站调车作业效率最大化",
                    "C. 人身安全及列车安全",
                    "D. 减少道岔转换次数"
                )
            ),
            answersJson = listToJson(listOf("C")),
            explanation = "综合安规真题强调人身安全与列车行车安全并重。",
            trapAnalysis = "需注意教材口径与安规真题口径差异。",
            mnemonic = "安全第一，预防为主",
            wrongCount = 1,
            isStarred = false,
            masteryPercent = 66,
            lastPracticedLabel = "3天前"
        ),
        // 16. Exam Bank - Exact Duplicate across banks for Merge Demo in Audit
        QuestionEntity(
            id = "Q016",
            bank = "《模拟测试题集》",
            chapter = "第二部分：运输组织真题",
            section = "运行图基本概念",
            type = "single",
            difficulty = "简单",
            score = 2.0,
            stem = "铁路运输组织中列车运行图的基本作用是什么？",
            optionsJson = listToJson(
                listOf(
                    "A. 全路组织列车运行的基础，综合性作业规划纲领",
                    "B. 仅用于客运车站时刻表的制定依据",
                    "C. 单纯记录机车车辆检修周期的统计表格",
                    "D. 仅供调度员临时调整车流的参考草图"
                )
            ),
            answersJson = listToJson(listOf("A")),
            explanation = "列车运行图是全路组织列车运行的基础，也是综合性的运输生产计划。",
            trapAnalysis = "与《铁路运输组织基础》Q001 题干与答案完全相同，支持跨库合并记录。",
            mnemonic = "运行图 = 全路组织列车运行基础",
            wrongCount = 0,
            isStarred = false,
            masteryPercent = 90,
            lastPracticedLabel = "5天前"
        ),
        // 17. Question missing explanation (for Audit "缺解析" filter)
        QuestionEntity(
            id = "Q017",
            bank = "《公路交通工程》",
            chapter = "第三章：道路交叉口信号控制",
            section = "韦伯斯特配时",
            type = "single",
            difficulty = "中等",
            score = 2.0,
            stem = "在韦伯斯特（Webster）信号配时优化公式 Co = (1.5L + 5) / (1 - Y) 中，参数 L 代表什么物理含义？",
            optionsJson = listToJson(
                listOf(
                    "A. 信号周期内的总损失时间（秒）",
                    "B. 交叉口进口道排队长度（米）",
                    "C. 绿灯间隔时间（黄灯+全红）",
                    "D. 饱和流率比之和"
                )
            ),
            answersJson = listToJson(listOf("A")),
            explanation = "在韦伯斯特信号交叉口配时模型中，L 为信号周期内的总损失时间（包括各关键相位绿灯初启动损失与黄灯末清空损失之和）。",
            trapAnalysis = "L 为周期总损失时间，Y 为各关键相位流量比 y_i 之和。",
            mnemonic = "1.5倍总损失加5，除以1减总流量比Y",
            wrongCount = 1,
            isStarred = false,
            masteryPercent = 60,
            lastPracticedLabel = "4天前"
        )
    )

    fun initialPracticeRecords(): List<PracticeRecordEntity> = listOf(
        PracticeRecordEntity(
            title = "《铁路运输组织基础》专项练习",
            bankName = "《铁路运输组织基础》",
            modeLabel = "顺序练习",
            dateLabel = "今天 14:20",
            scorePercent = 85,
            totalCount = 12,
            correctCount = 10,
            durationSec = 495
        ),
        PracticeRecordEntity(
            title = "艾宾浩斯今日错题巩固",
            bankName = "《高数与计算机体系结构》",
            modeLabel = "错题复习",
            dateLabel = "昨天 20:15",
            scorePercent = 75,
            totalCount = 8,
            correctCount = 6,
            durationSec = 380
        ),
        PracticeRecordEntity(
            title = "《公路交通工程》阶段模拟测验",
            bankName = "《公路交通工程》",
            modeLabel = "模拟考试",
            dateLabel = "3天前 16:40",
            scorePercent = 90,
            totalCount = 10,
            correctCount = 9,
            durationSec = 610
        )
    )

    fun initialPresets(): List<ImportPresetEntity> = listOf(
        ImportPresetEntity(
            name = "标准铁路专业题库清洗规则",
            customSerialRegex = "^\\s*(第?\\d+[题、.．)]|\\(\\d+\\))\\s*",
            multiChoiceDelimiter = "[,，、;；\\s]+",
            removeSerial = true,
            normalizeAnswer = true,
            inferType = true,
            mergeLines = true,
            skipEmpty = true
        ),
        ImportPresetEntity(
            name = "高校期末试卷 Excel 映射模板",
            customSerialRegex = "^\\s*\\d+\\.\\s*",
            multiChoiceDelimiter = "[,，\\s]+",
            removeSerial = true,
            normalizeAnswer = true,
            inferType = true,
            mergeLines = false,
            skipEmpty = true
        )
    )

    fun initialImportLogs(): List<ImportLogEntity> = listOf(
        ImportLogEntity(
            fileName = "2026铁路运输组织核心题库.xlsx",
            targetBank = "《铁路运输组织基础》",
            addedCount = 42,
            replacedCount = 3,
            skippedCount = 2,
            invalidCount = 0,
            dateLabel = "2天前 10:30"
        ),
        ImportLogEntity(
            fileName = "公路交通流理论补充习题.csv",
            targetBank = "《公路交通工程》",
            addedCount = 26,
            replacedCount = 1,
            skippedCount = 0,
            invalidCount = 0,
            dateLabel = "5天前 18:12"
        )
    )
}
