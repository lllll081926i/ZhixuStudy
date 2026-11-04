package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM question_banks ORDER BY updatedAt DESC")
    fun observeBanks(): Flow<List<QuestionBankEntity>>

    @Query("SELECT * FROM question_banks")
    suspend fun getAllBanks(): List<QuestionBankEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBank(bank: QuestionBankEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBanks(banks: List<QuestionBankEntity>)

    @Query("DELETE FROM question_banks WHERE name = :bankName")
    suspend fun deleteBankByName(bankName: String)

    @Query("UPDATE question_banks SET name = :newName WHERE name = :oldName")
    suspend fun renameBankEntity(oldName: String, newName: String)

    @Query("SELECT * FROM questions ORDER BY id ASC")
    fun observeAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions ORDER BY id ASC")
    suspend fun getAllQuestionsOnce(): List<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuestion(question: QuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuestions(questions: List<QuestionEntity>)

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteQuestionById(id: String)

    @Query("DELETE FROM questions WHERE id IN (:ids)")
    suspend fun deleteQuestionsByIds(ids: List<String>)

    @Query("DELETE FROM questions WHERE bank = :bankName")
    suspend fun deleteQuestionsByBank(bankName: String)

    @Query("UPDATE questions SET bank = :newName WHERE bank = :oldName")
    suspend fun moveQuestionsBank(oldName: String, newName: String)

    @Query("UPDATE questions SET chapter = :newChapter WHERE id IN (:ids)")
    suspend fun batchUpdateChapter(ids: List<String>, newChapter: String)

    @Query("UPDATE questions SET difficulty = :newDifficulty WHERE id IN (:ids)")
    suspend fun batchUpdateDifficulty(ids: List<String>, newDifficulty: String)

    @Query("SELECT * FROM practice_records ORDER BY timestamp DESC")
    fun observePracticeRecords(): Flow<List<PracticeRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPracticeRecord(record: PracticeRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPracticeRecords(records: List<PracticeRecordEntity>)

    @Query("UPDATE practice_records SET bankName = :newName WHERE bankName = :oldName")
    suspend fun updateRecordsBankName(oldName: String, newName: String)

    @Query("UPDATE import_logs SET targetBank = :newName WHERE targetBank = :oldName")
    suspend fun updateImportLogsBankName(oldName: String, newName: String)

    @Query("SELECT * FROM import_presets ORDER BY id DESC")
    fun observeImportPresets(): Flow<List<ImportPresetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImportPreset(preset: ImportPresetEntity)

    @Query("SELECT * FROM import_logs ORDER BY timestamp DESC")
    fun observeImportLogs(): Flow<List<ImportLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImportLog(log: ImportLogEntity)
}
